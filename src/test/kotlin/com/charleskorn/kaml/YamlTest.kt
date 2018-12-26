/*

   Copyright 2018 Charles Korn.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package com.charleskorn.kaml

import ch.tutteli.atrium.api.cc.en_GB.message
import ch.tutteli.atrium.api.cc.en_GB.notToBeNullBut
import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.api.cc.en_GB.toThrow
import ch.tutteli.atrium.verbs.assert
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.BooleanSerializer
import kotlinx.serialization.internal.ByteSerializer
import kotlinx.serialization.internal.CharSerializer
import kotlinx.serialization.internal.DoubleSerializer
import kotlinx.serialization.internal.EnumSerializer
import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.internal.IntSerializer
import kotlinx.serialization.internal.LongSerializer
import kotlinx.serialization.internal.ShortSerializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.internal.makeNullable
import kotlinx.serialization.list
import kotlinx.serialization.map
import kotlinx.serialization.serializer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object YamlTest : Spek({
    describe("a YAML parser") {
        describe("parsing scalars") {
            given("the input 'hello'") {
                val input = "hello"

                on("parsing that input as a string") {
                    val result = Yaml.parse(StringSerializer, input)

                    it("deserializes it to the expected string value") {
                        assert(result).toBe("hello")
                    }
                }

                on("parsing that input as a nullable string") {
                    val result = Yaml.parse(makeNullable(StringSerializer), input)

                    it("deserializes it to the expected string value") {
                        assert(result).notToBeNullBut("hello")
                    }
                }

                on("parsing that input with a serializer that uses YAML location information when throwing exceptions") {
                    it("throws an exception with the correct location information") {
                        assert({ Yaml.parse(LocationThrowingSerializer, input) }).toThrow<LocationInformationException> {
                            message { toBe("Serializer called with location: 1, 1") }
                        }
                    }
                }
            }

            given("the input '123'") {
                val input = "123"

                on("parsing that input as an integer") {
                    val result = Yaml.parse(Int.serializer(), input)

                    it("deserializes it to the expected integer") {
                        assert(result).toBe(123)
                    }
                }

                on("parsing that input as a long") {
                    val result = Yaml.parse(Long.serializer(), input)

                    it("deserializes it to the expected long") {
                        assert(result).toBe(123)
                    }
                }

                on("parsing that input as a short") {
                    val result = Yaml.parse(Short.serializer(), input)

                    it("deserializes it to the expected short") {
                        assert(result).toBe(123)
                    }
                }

                on("parsing that input as a byte") {
                    val result = Yaml.parse(Byte.serializer(), input)

                    it("deserializes it to the expected byte") {
                        assert(result).toBe(123)
                    }
                }

                on("parsing that input as a double") {
                    val result = Yaml.parse(Double.serializer(), input)

                    it("deserializes it to the expected double") {
                        assert(result).toBe(123.0)
                    }
                }

                on("parsing that input as a float") {
                    val result = Yaml.parse(FloatSerializer, input)

                    it("deserializes it to the expected float") {
                        assert(result).toBe(123.0f)
                    }
                }

                on("parsing that input as a nullable integer") {
                    val result = Yaml.parse(makeNullable(Int.serializer()), input)

                    it("deserializes it to the expected integer") {
                        assert(result).notToBeNullBut(123)
                    }
                }

                on("parsing that input as a nullable long") {
                    val result = Yaml.parse(makeNullable(Long.serializer()), input)

                    it("deserializes it to the expected long") {
                        assert(result).notToBeNullBut(123)
                    }
                }

                on("parsing that input as a nullable short") {
                    val result = Yaml.parse(makeNullable(Short.serializer()), input)

                    it("deserializes it to the expected short") {
                        assert(result).notToBeNullBut(123)
                    }
                }

                on("parsing that input as a nullable byte") {
                    val result = Yaml.parse(makeNullable(Byte.serializer()), input)

                    it("deserializes it to the expected byte") {
                        assert(result).notToBeNullBut(123)
                    }
                }

                on("parsing that input as a nullable double") {
                    val result = Yaml.parse(makeNullable(Double.serializer()), input)

                    it("deserializes it to the expected double") {
                        assert(result).notToBeNullBut(123.0)
                    }
                }

                on("parsing that input as a nullable float") {
                    val result = Yaml.parse(makeNullable(FloatSerializer), input)

                    it("deserializes it to the expected float") {
                        assert(result).notToBeNullBut(123.0f)
                    }
                }
            }

            given("the input 'true'") {
                val input = "true"

                on("parsing that input as a boolean") {
                    val result = Yaml.parse(BooleanSerializer, input)

                    it("deserializes it to the expected boolean value") {
                        assert(result).toBe(true)
                    }
                }

                on("parsing that input as a nullable boolean") {
                    val result = Yaml.parse(makeNullable(BooleanSerializer), input)

                    it("deserializes it to the expected boolean value") {
                        assert(result).notToBeNullBut(true)
                    }
                }
            }

            given("the input 'c'") {
                val input = "c"

                on("parsing that input as a character") {
                    val result = Yaml.parse(CharSerializer, input)

                    it("deserializes it to the expected character value") {
                        assert(result).toBe('c')
                    }
                }

                on("parsing that input as a nullable character") {
                    val result = Yaml.parse(makeNullable(CharSerializer), input)

                    it("deserializes it to the expected character value") {
                        assert(result).notToBeNullBut('c')
                    }
                }
            }

            mapOf(
                "Value1" to TestEnum.Value1,
                "Value2" to TestEnum.Value2
            ).forEach { input, expectedValue ->
                given("the input '$input'") {
                    on("parsing that input as an enumeration value") {
                        val result = Yaml.parse(EnumSerializer(TestEnum::class), input)

                        it("deserializes it to the expected enumeration value") {
                            assert(result).toBe(expectedValue)
                        }
                    }
                }
            }

            on("parsing an invalid enumeration value") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(EnumSerializer(TestEnum::class), "nonsense") }).toThrow<YamlScalarFormatException> {
                        message { toBe("Value 'nonsense' is not a valid option, permitted choices are: Value1, Value2") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }
        }

        describe("parsing null values") {
            val input = "null"

            on("parsing a null value as a nullable string") {
                val result = Yaml.parse(makeNullable(StringSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable string") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(StringSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable integer") {
                val result = Yaml.parse(makeNullable(IntSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable integer") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(IntSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable long") {
                val result = Yaml.parse(makeNullable(LongSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable long") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(LongSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable short") {
                val result = Yaml.parse(makeNullable(ShortSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable short") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(ShortSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable byte") {
                val result = Yaml.parse(makeNullable(ByteSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable byte") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(ByteSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable double") {
                val result = Yaml.parse(makeNullable(DoubleSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable double") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(DoubleSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable float") {
                val result = Yaml.parse(makeNullable(FloatSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable float") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(FloatSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable boolean") {
                val result = Yaml.parse(makeNullable(BooleanSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable boolean") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(BooleanSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable character") {
                val result = Yaml.parse(makeNullable(CharSerializer), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable character") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(CharSerializer, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable enum") {
                val result = Yaml.parse(makeNullable(EnumSerializer(TestEnum::class)), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable enum") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(EnumSerializer(TestEnum::class), input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable list") {
                val result = Yaml.parse(makeNullable(StringSerializer.list), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable list") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(StringSerializer.list, input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value as a nullable object") {
                val result = Yaml.parse(makeNullable(ComplexStructure.serializer()), input)

                it("returns a null value") {
                    assert(result).toBe(null)
                }
            }

            on("parsing a null value as a non-nullable object") {
                it("throws an appropriate exception") {
                    assert({ Yaml.parse(ComplexStructure.serializer(), input) }).toThrow<UnexpectedNullValueException> {
                        message { toBe("Unexpected null or empty value for non-null field.") }
                        line { toBe(1) }
                        column { toBe(1) }
                    }
                }
            }

            on("parsing a null value with a serializer that uses YAML location information when throwing exceptions") {
                it("throws an exception with the correct location information") {
                    assert({ Yaml.parse(LocationThrowingSerializer, input) }).toThrow<LocationInformationException> {
                        message { toBe("Serializer called with location: 1, 1") }
                    }
                }
            }
        }

        describe("parsing lists") {
            given("a list of strings") {
                val input = """
                    - thing1
                    - thing2
                    - thing3
                """.trimIndent()

                on("parsing that input as a list") {
                    val result = Yaml.parse(String.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf("thing1", "thing2", "thing3"))
                    }
                }

                on("parsing that input with a serializer that uses YAML location information when throwing exceptions") {
                    it("throws an exception with the correct location information") {
                        assert({ Yaml.parse(LocationThrowingSerializer.list, input) }).toThrow<LocationInformationException> {
                            message { toBe("Serializer called with location: 1, 3") }
                        }
                    }
                }
            }

            given("a list of numbers") {
                val input = """
                    - 123
                    - 45
                    - 6
                """.trimIndent()

                on("parsing that input as a list of integers") {
                    val result = Yaml.parse(Int.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(123, 45, 6))
                    }
                }

                on("parsing that input as a list of longs") {
                    val result = Yaml.parse(Long.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(123L, 45, 6))
                    }
                }

                on("parsing that input as a list of shorts") {
                    val result = Yaml.parse(Short.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(123.toShort(), 45, 6))
                    }
                }

                on("parsing that input as a list of bytes") {
                    val result = Yaml.parse(Byte.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(123.toByte(), 45, 6))
                    }
                }

                on("parsing that input as a list of doubles") {
                    val result = Yaml.parse(Double.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(123.0, 45.0, 6.0))
                    }
                }

                on("parsing that input as a list of floats") {
                    val result = Yaml.parse(FloatSerializer.list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(123.0f, 45.0f, 6.0f))
                    }
                }
            }

            given("a list of booleans") {
                val input = """
                    - true
                    - false
                """.trimIndent()

                on("parsing that input as a list") {
                    val result = Yaml.parse(Boolean.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(true, false))
                    }
                }
            }

            given("a list of enum values") {
                val input = """
                    - Value1
                    - Value2
                """.trimIndent()

                on("parsing that input as a list") {
                    val result = Yaml.parse(EnumSerializer(TestEnum::class).list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf(TestEnum.Value1, TestEnum.Value2))
                    }
                }
            }

            given("a list of characters") {
                val input = """
                    - a
                    - b
                """.trimIndent()

                on("parsing that input as a list") {
                    val result = Yaml.parse(CharSerializer.list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf('a', 'b'))
                    }
                }
            }

            given("a list of nullable strings") {
                val input = """
                    - thing1
                    - null
                """.trimIndent()

                on("parsing that input as a list") {
                    val result = Yaml.parse(makeNullable(String.serializer()).list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(listOf("thing1", null))
                    }
                }
            }

            given("a list of lists") {
                val input = """
                    - [thing1, thing2]
                    - [thing3]
                """.trimIndent()

                on("parsing that input as a list") {
                    val result = Yaml.parse(String.serializer().list.list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(
                            listOf(
                                listOf("thing1", "thing2"),
                                listOf("thing3")
                            )
                        )
                    }
                }
            }

            given("a list of objects") {
                val input = """
                    - name: thing1
                    - name: thing2
                """.trimIndent()

                on("parsing that input as a list") {
                    val result = Yaml.parse(SimpleStructure.serializer().list, input)

                    it("deserializes it to the expected value") {
                        assert(result).toBe(
                            listOf(
                                SimpleStructure("thing1"),
                                SimpleStructure("thing2")
                            )
                        )
                    }
                }
            }
        }

        describe("parsing objects") {
            given("some input representing an object with an optional value specified") {
                val input = """
                    string: Alex
                    byte: 12
                    short: 1234
                    int: 123456
                    long: 1234567
                    float: 1.2
                    double: 2.4
                    enum: Value1
                    boolean: true
                    char: A
                    nullable: present
                """.trimIndent()

                on("parsing that input") {
                    val result = Yaml.parse(ComplexStructure.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(
                            ComplexStructure(
                                "Alex",
                                12,
                                1234,
                                123456,
                                1234567,
                                1.2f,
                                2.4,
                                TestEnum.Value1,
                                true,
                                'A',
                                "present"
                            )
                        )
                    }
                }
            }

            given("some input representing an object with an optional value specified as null") {
                val input = """
                    string: Alex
                    byte: 12
                    short: 1234
                    int: 123456
                    long: 1234567
                    float: 1.2
                    double: 2.4
                    enum: Value1
                    boolean: true
                    char: A
                    nullable: null
                """.trimIndent()

                on("parsing that input") {
                    val result = Yaml.parse(ComplexStructure.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(
                            ComplexStructure(
                                "Alex",
                                12,
                                1234,
                                123456,
                                1234567,
                                1.2f,
                                2.4,
                                TestEnum.Value1,
                                true,
                                'A',
                                null
                            )
                        )
                    }
                }
            }

            given("some input representing an object with an optional value not specified") {
                val input = """
                    string: Alex
                    byte: 12
                    short: 1234
                    int: 123456
                    long: 1234567
                    float: 1.2
                    double: 2.4
                    enum: Value1
                    boolean: true
                    char: A
                """.trimIndent()

                on("parsing that input") {
                    val result = Yaml.parse(ComplexStructure.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(
                            ComplexStructure(
                                "Alex",
                                12,
                                1234,
                                123456,
                                1234567,
                                1.2f,
                                2.4,
                                TestEnum.Value1,
                                true,
                                'A',
                                null
                            )
                        )
                    }
                }
            }

            given("some input representing an object with an embedded list") {
                val input = """
                        members:
                            - Alex
                            - Jamie
                    """.trimIndent()

                on("parsing that input") {
                    val result = Yaml.parse(Team.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(Team(listOf("Alex", "Jamie")))
                    }
                }
            }

            given("some input representing an object with an embedded object") {
                val input = """
                        firstPerson:
                            name: Alex
                        secondPerson:
                            name: Jamie
                    """.trimIndent()

                on("parsing that input") {
                    val result = Yaml.parse(NestedObjects.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(NestedObjects(SimpleStructure("Alex"), SimpleStructure("Jamie")))
                    }
                }
            }

            given("some input representing an object where the keys are in a different order to the object definition") {
                val input = """
                        secondPerson:
                            name: Jamie
                        firstPerson:
                            name: Alex
                    """.trimIndent()

                on("parsing that input") {
                    val result = Yaml.parse(NestedObjects.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(NestedObjects(SimpleStructure("Alex"), SimpleStructure("Jamie")))
                    }
                }
            }

            given("some input representing an object with an unknown key") {
                val input = """
                        abc123: something
                    """.trimIndent()

                on("parsing that input") {
                    it("throws an appropriate exception") {
                        assert({ Yaml.parse(ComplexStructure.serializer(), input) }).toThrow<UnknownPropertyException> {
                            message { toBe("Unknown property 'abc123'. Known properties are: boolean, byte, char, double, enum, float, int, long, nullable, short, string") }
                            line { toBe(1) }
                            column { toBe(1) }
                            propertyName { toBe("abc123") }
                            validPropertyNames { toBe(setOf("boolean", "byte", "char", "double", "enum", "float", "int", "long", "nullable", "short", "string")) }
                        }
                    }
                }
            }

            given("some input representing an object with a list as a key") {
                val input = """
                        []: something
                    """.trimIndent()

                on("parsing that input") {
                    it("throws an appropriate exception") {
                        assert({ Yaml.parse(ComplexStructure.serializer(), input) }).toThrow<MalformedYamlException> {
                            message { toBe("Property name must not be a list, map or null value. (To use 'null' as a property name, enclose it in quotes.)") }
                            line { toBe(1) }
                            column { toBe(1) }
                        }
                    }
                }
            }

            given("some input representing an object with an invalid value for a field") {
                mapOf(
                    "byte" to "Value 'xxx' is not a valid byte value.",
                    "short" to "Value 'xxx' is not a valid short value.",
                    "int" to "Value 'xxx' is not a valid integer value.",
                    "long" to "Value 'xxx' is not a valid long value.",
                    "float" to "Value 'xxx' is not a valid floating point value.",
                    "double" to "Value 'xxx' is not a valid floating point value.",
                    "enum" to "Value 'xxx' is not a valid option, permitted choices are: Value1, Value2",
                    "boolean" to "Value 'xxx' is not a valid boolean, permitted choices are: true or false",
                    "char" to "Value 'xxx' is not a valid character value."
                ).forEach { fieldName, errorMessage ->
                    given("the invalid field represents a $fieldName") {
                        val input = "$fieldName: xxx"

                        on("parsing that input") {
                            it("throws an appropriate exception") {
                                assert({ Yaml.parse(ComplexStructure.serializer(), input) }).toThrow<InvalidPropertyValueException> {
                                    message { toBe("Value for '$fieldName' is invalid: $errorMessage") }
                                    line { toBe(1) }
                                    column { toBe(fieldName.length + 3) }
                                    propertyName { toBe(fieldName) }
                                    reason { toBe(errorMessage) }
                                }
                            }
                        }
                    }
                }
            }

            given("some input representing an object with a null value for a non-nullable scalar field") {
                val input = "name: null"

                on("parsing that input") {
                    it("throws an appropriate exception") {
                        assert({ Yaml.parse(SimpleStructure.serializer(), input) }).toThrow<InvalidPropertyValueException> {
                            message { toBe("Value for 'name' is invalid: Unexpected null or empty value for non-null field.") }
                            line { toBe(1) }
                            column { toBe(7) }
                            propertyName { toBe("name") }
                            reason { toBe("Unexpected null or empty value for non-null field.") }
                        }
                    }
                }
            }

            given("some input representing an object with a null value for a non-nullable nested object field") {
                val input = "firstPerson: null"

                on("parsing that input") {
                    it("throws an appropriate exception") {
                        assert({ Yaml.parse(NestedObjects.serializer(), input) }).toThrow<InvalidPropertyValueException> {
                            message { toBe("Value for 'firstPerson' is invalid: Unexpected null or empty value for non-null field.") }
                            line { toBe(1) }
                            column { toBe(14) }
                            propertyName { toBe("firstPerson") }
                            reason { toBe("Unexpected null or empty value for non-null field.") }
                        }
                    }
                }
            }

            given("some input representing an object with a null value for a nullable nested object field") {
                @Serializable
                data class NullableNestedObject(val firstPerson: SimpleStructure?)

                val input = "firstPerson: null"

                on("parsing that input") {
                    val result = Yaml.parse(NullableNestedObject.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(NullableNestedObject(null))
                    }
                }
            }

            given("some input representing an object with a null value for a non-nullable nested list field") {
                val input = "members: null"

                on("parsing that input") {
                    it("throws an appropriate exception") {
                        assert({ Yaml.parse(Team.serializer(), input) }).toThrow<InvalidPropertyValueException> {
                            message { toBe("Value for 'members' is invalid: Unexpected null or empty value for non-null field.") }
                            line { toBe(1) }
                            column { toBe(10) }
                            propertyName { toBe("members") }
                            reason { toBe("Unexpected null or empty value for non-null field.") }
                        }
                    }
                }
            }

            given("some input representing an object with a null value for a nullable nested list field") {
                @Serializable
                data class NullableNestedList(val members: List<String>?)

                val input = "members: null"

                on("parsing that input") {
                    val result = Yaml.parse(NullableNestedList.serializer(), input)

                    it("deserializes it to a Kotlin object") {
                        assert(result).toBe(NullableNestedList(null))
                    }
                }
            }

            given("some input representing an object with a custom serializer for one of its values") {
                val input = "value: something"

                on("parsing that input with a serializer that uses YAML location information when throwing exceptions") {
                    it("throws an exception with the correct location information") {
                        assert({ Yaml.parse(StructureWithLocationThrowingSerializer.serializer(), input) }).toThrow<LocationInformationException> {
                            message { toBe("Serializer called with location: 1, 8") }
                        }
                    }
                }
            }

            given("some input representing a generic map") {
                val input = """
                    SOME_ENV_VAR: somevalue
                    SOME_OTHER_ENV_VAR: someothervalue
                """.trimIndent()

                on("parsing that input") {
                    val result = Yaml.parse((StringSerializer to StringSerializer).map, input)

                    it("deserializes it to a Kotlin map") {
                        assert(result).toBe(
                            mapOf(
                                "SOME_ENV_VAR" to "somevalue",
                                "SOME_OTHER_ENV_VAR" to "someothervalue"
                            )
                        )
                    }
                }

                on("parsing that input with a serializer for the key that uses YAML location information when throwing exceptions") {
                    it("throws an exception with the correct location information") {
                        assert({ Yaml.parse((LocationThrowingSerializer to StringSerializer).map, input) }).toThrow<LocationInformationException> {
                            message { toBe("Serializer called with location: 1, 1") }
                        }
                    }
                }

                on("parsing that input with a serializer for the value that uses YAML location information when throwing exceptions") {
                    it("throws an exception with the correct location information") {
                        assert({ Yaml.parse((StringSerializer to LocationThrowingSerializer).map, input) }).toThrow<LocationInformationException> {
                            message { toBe("Serializer called with location: 1, 15") }
                        }
                    }
                }
            }
        }
    }
})

@Serializable
data class SimpleStructure(
    val name: String
)

@Serializable
data class ComplexStructure(
    val string: String,
    val byte: Byte,
    val short: Short,
    val int: Int,
    val long: Long,
    val float: Float,
    val double: Double,
    val enum: TestEnum,
    val boolean: Boolean,
    val char: Char,
    @Optional val nullable: String? = null
)

@Serializable
data class Team(
    val members: List<String>
)

@Serializable
data class NestedObjects(
    val firstPerson: SimpleStructure,
    val secondPerson: SimpleStructure
)

@Serializable
data class StructureWithLocationThrowingSerializer(
    @Serializable(with = LocationThrowingSerializer::class) val value: CustomSerializedValue
)

data class CustomSerializedValue(val thing: String)

enum class TestEnum {
    Value1,
    Value2
}

@Serializer(forClass = CustomSerializedValue::class)
object LocationThrowingSerializer : KSerializer<CustomSerializedValue> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor

    override fun deserialize(input: Decoder): CustomSerializedValue {
        val location = (input as YamlInput).getCurrentLocation()

        throw LocationInformationException("Serializer called with location: ${location.line}, ${location.column}")
    }

    override fun serialize(output: Encoder, obj: CustomSerializedValue) = throw UnsupportedOperationException()
}

class LocationInformationException(message: String) : RuntimeException(message)