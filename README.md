Vert.x code translator :
========

This projects aims to translate Java code using the Vert.x API in another languages whenever it's possible.

Supported languages:

* Groovy
* JavaScript


## Todo

- codegen propeties for projection
- handle case for handlers that are not the last parameter (MUST)
- handle parenthesized expression in String concat : `1 + (2 + "_")` is rewritten as `1 + ("${2}_")` instead of ???
- try to find something for Ruby postfix increment/decrement (is that even possible?)
