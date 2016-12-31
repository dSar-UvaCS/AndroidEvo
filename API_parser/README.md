# API Parser
Parses Android API change documentations for changes from Package level down to Field level between versions.

## Code
*ChangeDetail.java parses online change documentations, *ChangeDetailsArchivedVersions.java parses change documentations of older versions that require input files.

## Input
"input" folder contains unzipped folders containing files of older versions of change documentations used by *ChangeDetailsArchivedVersions.java.

## Output
Output files are in .csv formats and placed in "output" folder, each file contains seven columns:

#### API_version:
Version where this change occurred
####element_type:
Type of element that is changed (Package, Class, Method, Constructor, or Field)
####element_name:
Name of element that is changed
####element_type_change_type:
Type of change that occured (Change: some part of the element is changed; Added: the element is newly added; Removed: the element is removed from the API; Deprecated: the element is deprecated)
####changed_element_type:
Type of the part of the element that is changed
```
Example:
element_type: Packages, changed_element_type: Class
element_type: Classes, changed_element_type: Method
element_type: Methods, changed_element_type: Keyword
element_type: Methods, changed_element_type: Exceptions
```
####changed_element_name:
Name of the part of the element that is changed
####changed_element_modification_type
Type of change of the part of the element that is changed
```
Example:
changed_element_type: Keyword, changed_element_type: Change
changed_element_type: Exceptions, changed_element_type: Change
changed_element_type: Method, changed_element_type: Added
changed_element_type: Class, changed_element_type: Removed
```
