///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package test.depreciated;
//
//import com.jme3.input.KeyInput;
//import com.jme3.input.MouseInput;
//
///**
// * A Default List of Button Mappings
// * @author Kyle Williams
// */
//public class ButtonLayout {    
//
//    public static void main(String[] args){
//        ButtonLayout a = new ButtonLayout();
//        a.ATTACK1.setMapping(1);
//        ButtonLayout b = new ButtonLayout();
//        b.ATTACK1.setMapping(6);
//
//        System.out.println(a.ATTACK1.getMappedButton());
//        System.out.println(b.ATTACK1.getMappedButton());
//    }
//
//      public final Buttons CAMERA_X = new Buttons("CAMERA_X",MouseInput.AXIS_X);
//      public final Buttons CAMERA_Y = new Buttons("CAMERA_X",MouseInput.AXIS_Y);
//      public final Buttons CENTERCAMERA = new Buttons("CAMERA_X",MouseInput.BUTTON_RIGHT);
//      public final Buttons LOCKON = new Buttons("CAMERA_X",MouseInput.BUTTON_LEFT);
//      public final Buttons ATTACK1 = new Buttons("CAMERA_X",KeyInput.KEY_1);
//      public final Buttons ATTACK2 = new Buttons("CAMERA_X",KeyInput.KEY_2);
//      public final Buttons ATTACK3 = new Buttons("CAMERA_X",KeyInput.KEY_3);
//      public final Buttons ATTACK4 = new Buttons("CAMERA_X",KeyInput.KEY_4);
//      public final Buttons FORWARD = new Buttons("CAMERA_X",KeyInput.KEY_W);
//      public final Buttons BACKWARD = new Buttons("CAMERA_X",KeyInput.KEY_S);
//      public final Buttons LEFT = new Buttons("CAMERA_X",KeyInput.KEY_A);
//      public final Buttons RIGHT = new Buttons("CAMERA_X",KeyInput.KEY_D);
//
//
//      private final Buttons[] values = new Buttons[] {CAMERA_X, CAMERA_Y,CENTERCAMERA,
//                                    LOCKON,ATTACK1,ATTACK2,ATTACK3,ATTACK4,FORWARD,BACKWARD,LEFT,RIGHT };
//
//      public Buttons[] values() { return values.clone(); }
//      //public static ButtonLayout valueOf(String name) { return Enum.valueOf(ButtonLayout.class, name); }
//
//    private class Buttons{
//        private String Name;
//        private int inputValue;
//
//        private Buttons(String name, int input) {
//            this.Name = name;
//            this.inputValue = input;
//        }
//
//        /**
//         * Returns the buttonMappings
//         * @return
//         */
//        public int getMappedButton(){return inputValue;}
//
//        /**
//         * Sets the Button Mappings
//         * It should Be noted that CAMERA_X & CAMERA_Y
//         * should be mapped to an axis such as a mouse or joystick
//         * @param input
//         */
//        public void setMapping(int input){
//            inputValue=input;
//        }
//    }
//}
//
