package org.pageseeder.schematron.svrl;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

/**
 * SVRL data stored as a string.
 *
 * @author Christophe Lauret
 *
 * @version 2.1.1
 * @since 2.1.1
 */
public final class SVRLDataFile implements SVRLData {

  private final File _svrl;

  private final Charset _charset;

  public SVRLDataFile(File svrl, Charset charset) {
    this._svrl = Objects.requireNonNull(svrl);
    this._charset = Objects.requireNonNull(charset);
  }

  @Override
  public String asString() {
    return new String(asByteArray(), this._charset);
  }

  @Override
  public byte[] asByteArray() {
    try {
      return Files.readAllBytes(this._svrl.toPath());
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  @Override
  public Reader getReader() throws IOException {
    return new InputStreamReader(new FileInputStream(this._svrl), this._charset);
  }

}
