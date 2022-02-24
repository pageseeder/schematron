/*
 * Copyright 2022 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.schematron.svrl;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * SVRL data stored as a string.
 *
 * @author Christophe Lauret
 *
 * @version 2.1.1
 * @since 2.1.1
 */
public final class SVRLByteArray implements SVRLData {

  private final byte[] _svrl;

  private final Charset _charset;

  public SVRLByteArray(byte[] svrl) {
    this(svrl, StandardCharsets.UTF_8);
  }

  public SVRLByteArray(byte[] svrl, Charset charset) {
    this._svrl = Objects.requireNonNull(svrl);
    this._charset = Objects.requireNonNull(charset);
  }

  @Override
  public String asString() {
    return new String(this._svrl, this._charset);
  }

  @Override
  public byte[] asByteArray() {
    return this._svrl;
  }

  @Override
  public Reader getReader() {
    return new InputStreamReader(new ByteArrayInputStream(this._svrl), this._charset);
  }
}
