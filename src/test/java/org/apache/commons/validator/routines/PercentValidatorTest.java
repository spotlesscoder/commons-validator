/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.validator.routines;

import java.math.BigDecimal;
import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Test;
/**
 * Test Case for PercentValidator.
 */
public class PercentValidatorTest extends TestCase {

    protected PercentValidator validator;

    /**
     * Constructor
     * @param name test name
     */
    public PercentValidatorTest(final String name) {
        super(name);
    }

    @Override
    protected void setUp() {
        validator = new PercentValidator();
    }

    /**
     * Tear down
     */
    @Override
    protected void tearDown() {
        validator = null;
    }

    /**
     * Test Format Type
     */
    @Test
    public void testFormatType() {
        assertEquals("Format Type A", 2, PercentValidator.getInstance().getFormatType());
        assertEquals("Format Type B", AbstractNumberValidator.PERCENT_FORMAT, PercentValidator.getInstance().getFormatType());
    }

    /**
     * Test Invalid percentage values
     */
    @Test
    public void testInvalid() {
        final BigDecimalValidator validator = PercentValidator.getInstance();

        // Invalid Missing
        assertFalse("isValid() Null Value",    validator.isValid(null));
        assertFalse("isValid() Empty Value",   validator.isValid(""));
        assertNull("validate() Null Value",    validator.validate(null));
        assertNull("validate() Empty Value",   validator.validate(""));

        // Invalid UK
        assertFalse("UK wrong symbol",    validator.isValid("12@",   Locale.UK)); // ???
        assertFalse("UK wrong negative",  validator.isValid("(12%)", Locale.UK));

        // Invalid US - can't find a Locale with different symbols!
        assertFalse("US wrong symbol",    validator.isValid("12@",   Locale.US)); // ???
        assertFalse("US wrong negative",  validator.isValid("(12%)", Locale.US));
    }

    /**
     * Test Valid percentage values
     */
    @Test
    public void testValid() {
        // Set the default Locale
        final Locale origDefault = Locale.getDefault();
        Locale.setDefault(Locale.UK);

        final BigDecimalValidator validator = PercentValidator.getInstance();
        final BigDecimal expected = new BigDecimal("0.12");
        final BigDecimal negative = new BigDecimal("-0.12");
        final BigDecimal hundred  = new BigDecimal("1.00");

        assertEquals("Default locale", expected, validator.validate("12%"));
        assertEquals("Default negtve", negative, validator.validate("-12%"));

        // Invalid UK
        assertEquals("UK locale",      expected, validator.validate("12%",   Locale.UK));
        assertEquals("UK negative",    negative, validator.validate("-12%",  Locale.UK));
        assertEquals("UK No symbol",   expected, validator.validate("12",    Locale.UK));

        // Invalid US - can't find a Locale with different symbols!
        assertEquals("US locale",      expected, validator.validate("12%",   Locale.US));
        assertEquals("US negative",    negative, validator.validate("-12%",  Locale.US));
        assertEquals("US No symbol",   expected, validator.validate("12",    Locale.US));

        assertEquals("100%",           hundred, validator.validate("100%"));

        // Restore the original default
        Locale.setDefault(origDefault);
    }

}
