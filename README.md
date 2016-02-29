# Dagger Intellij Plugin

Dagger and Dagger2 are using JSR-330 specification to determine dependency injection in code. Main goal of this plugin is to increase productivity and happyness during programming with Daggers.

## Wrapper around Intellij
We should not try to implement better code generators which are already implemented in [IntelliJ Community][1]. This is *not* goal of this plugin. We should utilize already existing features of IntelliJ codebase and wrap them to add required features. That's why, `ConstructorJsr330` generator is just a wrapper around IntelliJ constructor generator. What this generator do under the hood is annotating such generated constructor with `@Inject` annotation.

## Features
- generating constructor annotated with @Inject

# License
    Copyright 2016 Pawel Hajduk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: https://github.com/JetBrains/intellij-community
