package org.example;

public class AnnotationsIntro {
    public static void main(String[] args) throws NoSuchMethodException {

        // @Override at SuperUser's setPassword() - RetentionPolicy.SOURCE (see F12)
        // => not visible out of the code, i.e. for Reflection, either
        Override anno = Reflections.SuperUser.class
                .getMethod("setPassword", String.class)
                .getAnnotation(Override.class);
        System.out.println(anno); // -> null


        // MyAnnotation - RetentionPolicy.RUNTIME => can be read by Reflection
        MyAnnotation myAnno = Reflections.SuperUser.class
                .getMethod("setPassword", String.class)
                .getAnnotation(MyAnnotation.class);
        System.out.println(myAnno); // -> MyAnnotation
        System.out.println(myAnno.myParameter()); // see "text", as declared in @MyAnnotation before Reflections.SuperUser's setPassword()


    }

}
