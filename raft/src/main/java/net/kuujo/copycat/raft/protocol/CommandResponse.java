/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.raft.protocol;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Protocol command response.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class CommandResponse extends AbstractResponse {

  /**
   * Returns a new command response builder.
   *
   * @return A new command response builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Returns a command response builder for an existing response.
   *
   * @param response The response to build.
   * @return The command response builder.
   */
  public static Builder builder(CommandResponse response) {
    return new Builder(response);
  }

  private ByteBuffer result;

  /**
   * Returns the command result.
   *
   * @return The command result.
   */
  public ByteBuffer result() {
    return result;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, status, result);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof CommandResponse) {
      CommandResponse response = (CommandResponse) object;
      return response.id.equals(id)
        && response.status == status
        && ((response.result == null && result == null)
        || response.result != null && result != null && response.result.equals(result));
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s[status=%s, result=%s]", getClass().getSimpleName(), status, result);
  }

  /**
   * Command response builder.
   */
  public static class Builder extends AbstractResponse.Builder<Builder, CommandResponse> {
    private Builder() {
      this(new CommandResponse());
    }

    private Builder(CommandResponse response) {
      super(response);
    }

    /**
     * Sets the command response result.
     *
     * @param result The response result.
     * @return The response builder.
     */
    public Builder withResult(ByteBuffer result) {
      response.result = result;
      return this;
    }

    @Override
    public int hashCode() {
      return Objects.hash(response);
    }

    @Override
    public boolean equals(Object object) {
      return object instanceof Builder && ((Builder) object).response.equals(response);
    }

    @Override
    public String toString() {
      return String.format("%s[response=%s]", getClass().getCanonicalName(), response);
    }

  }

}
