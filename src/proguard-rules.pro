# Add any ProGuard configurations specific to this
-dontwarn java.lang.invoke.**
# extension here.

-keep public class com.bextdev.Texty.Texty {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/bextdev/Texty/repack'
-flattenpackagehierarchy
-dontpreverify
