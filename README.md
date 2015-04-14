Vert.x code translator :
========

This projects aims to translate Java code using the Vert.x API in another languages whenever it's possible.

Supported languages:

* Groovy
* JavaScript


## Todo

Handle parenthesized expression in String concat : `1 + (2 + "_")` is rewritten as `1 + ("${2}_")` instead of ???