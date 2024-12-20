-keep public class ph.bextdev.Texty.Texty {
    public *;
}
-keep class java.lang.invoke.StringConcatFactory { *; }
-keep class gnu.kawa** { *; }
-keep class gnu.expr** { *; }

-dontwarn java.lang.invoke.StringConcatFactory
-optimizations aggressive
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'ph/bextdev/Texty/repack'
-flattenpackagehierarchy
-dontpreverify
