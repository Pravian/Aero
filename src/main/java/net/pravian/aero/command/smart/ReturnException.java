/*
 * Copyright 2015 Pravian Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pravian.aero.command.smart;

import lombok.Getter;

public class ReturnException extends ArgumentException {

    private static final long serialVersionUID = -29222346432700134L;

    @Getter
    private final boolean returnValue;

    public ReturnException(boolean returnValue) {
        this(null, returnValue);
    }

    public ReturnException(String message, boolean returnValue) {
        super(message);
        this.returnValue = returnValue;
    }
}
