##Workshop Notes

- ~~Compare Malli to Spec~~
- Look at the available clojure.core functions that work with Malli:  
  - with schemas select-keys, dissoc, get, assoc, update,
 get-in, assoc-in, update-in, merge, union
  - predicates any?, some?, number?, integer?, int?, pos-int?, neg-int?, nat-int?, float?, double?, boolean?, string?, ident?, simple-ident?, qualified-ident?, keyword?, simple-keyword?, qualified-keyword?, symbol?, simple-symbol?, qualified-symbol?, uuid?, uri?, decimal?, inst?, seqable?, indexed?, map?, vector?, list?, seq?, char?, set?, nil?, false?, true?, zero?, rational?, coll?, empty?, associative?, sequential?, ratio? and bytes?
- Malli functions:
  - Comparators :>, :>=, :<, :<=, := and :not=
  - Type like :any, :nil, :string, :int, :double, :boolean, :keyword, :symbol, :qualified-symbol, :qualified-keyword and :uuid
  - Regex :+, :*, :?, :repeat, :cat, :alt, :catn, :altn
  - Contains :and, :or, :not, :map, :map-of, :vector, :sequential, :set, :tuple, :enum, :maybe, :multi, :re and :fn
- Generate values with schemas
- Parsing & unparsing
- Convert schemas to maps, edn, swagger2, json
- Registries
- Function schemas
- Function schema registries
- Use with clj-kondo (linter)
- Performance testing


##References
- https://github.com/metosin/malli#value-generation
- https://www.metosin.fi/blog/malli/