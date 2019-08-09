
## Overview

BASE is a Java to Java ETL code generator alternative to ORM and enterprise ETL.  BASE is focused on providing complete execution  transparency and control.

## Summary

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
BASE’s output is a operational, self-contained software project including all of the
SQL definitions, parsers, models, data layers, API endpoints and clients required to perform ETL on the data of
the form referenced in Phase 1 reflecting the operator specifications in Phase 2 and Phase 3.
If additional details would be helpful, feel free to contact me as I am happy to provide clarification.

## Set up

```bash
# Install Maven if needed
# sudo apt-get install maven
git clone git@github.com:kamrankashef/BASE.git
cd BASE
mvn compile
```

