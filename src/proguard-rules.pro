# Add any ProGuard configurations specific to this
# extension here.

-keep public class ph.bxtdev.Texty.Texty {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'ph/bxtdev/Texty/repack'
-flattenpackagehierarchy
-dontpreverify
