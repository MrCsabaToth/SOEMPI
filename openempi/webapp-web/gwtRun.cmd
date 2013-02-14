@REM
@REM
@REM  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM     http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
@REM  implied. See the License for the specific language governing
@REM  permissions and limitations under the License.
@REM
@REM
@echo off
mvn -Dlog4j.debug=true -Dgwt.module=org.openempi.webapp.Application gwt:run

