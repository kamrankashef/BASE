BASE is a Java to Java ETL code generator alternative to ORM and enterprise ETL.
BASE is focused on providing complete execution  transparency and control.

BASE can analyze several data formats (e.g. XML, Json or delimited), infer its structure and create a corresponding SQL table
definition, parsers, class objects and data-layer classes.  The user has be ability to override default behavior such as
how objects and attributes are named, typing and combining/mutating objects and fields in custom ways.

- [Setup](#setup)
- [Simple Example](#simple-example)
- [What's Coming Next](#whats-coming-next)
- [Execution Phases](#execution-phases)

## Setup

```bash
# Install Maven if needed
# sudo apt-get install maven
git clone git@github.com:kamrankashef/BASE.git
cd BASE
mvn compile
mvn test
# Ignore echoed exception, you will see:
# https://github.com/kamrankashef/BASE/blob/master/src/test/resources/fromjson/application.json
# Drive the creation of /tmp/capfriendly
# Modify the application.json file, rerun mvn test and see you changes passed through
```
The test target generates the sample applications in `expected_out`.  Running and modifying the test cases are a great
place to start learning BASE.

## Simple Example

In [the Player Scouting example](https://github.com/kamrankashef/BASE/blob/master/src/test/java/base/parsegen/csv/playerscouting/TestPlayerScoutingCSV.java), we are given an a [CSV document](https://github.com/kamrankashef/BASE/blob/master/src/test/resources/base/parsegen/csv/playerscouting/sample-export.csv) with over 40 fields.  A little custom behavior is specified, such as adding date-typed verions of some fields in `getModelAugmenterI`, using a special naming functions that can convert field names like `Shot %` to `shot_p` and specify `playerscouting` as the name of our desired export package.

Running the test provides this [Maven application](https://github.com/kamrankashef/BASE/tree/master/expected_out/player_scouting/application).
The user can the go into the [generated parser](https://github.com/kamrankashef/BASE/blob/master/expected_out/player_scouting/application/src/main/java/main/PlayerScoutingFeedParser.java)
and determine where to invoke the `insert` method from the 
[generated persistence-layer](https://github.com/kamrankashef/BASE/blob/master/expected_out/player_scouting/application/src/main/java/playerscouting/derived/datalayer/PlayerScoutingDL.java).

## What's Coming Next

- Additional regression testing
- Use an SQL table as the data source
- BASE is due for a clean up to make it easier to work with for new users
- Add support for multiple target languages, starting with Python
- A GUI

## Phases of Execution

BASE is a data management tool that automates the creation of ETL software with four primary
phases of execution:

#### Phase 1 - Analysis
BASE analyzes data schemas as well as structured and semi-structured data to infer an
intermediate model representation of the data structure. This structure include names, data-types,
relationships and consistency constraints.

#### Phase 2 - Mutation
BASE allows the operator to override any naming, typing, relationships, and consistency
constraints established in Phase 1 via predefined and extensible hooks.

#### Phase 3 - Output Specification
BASE allows the user to specify output artifacts such as parsers specific to
each model, model class representations, model methods, data access layers classes and data access layer
methods, SQL schemas, REST API CRUD endpoints and API clients. BASE’s output specification is
extensible allowing the operator to choose from predefined artifact types as well operator defined artifacts.

#### Phase 4 - Code Generation
BASE’s output is an operational, self-contained software project including all of the
SQL definitions, parsers, models, data layers, API endpoints and clients required to perform ETL on the data of
the form referenced in Phase 1 reflecting the operator specifications in Phase 2 and Phase 3.

