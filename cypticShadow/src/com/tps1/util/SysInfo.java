package com.tps1.util;
 
import com.jmex.audio.AudioSystem;

import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

/**
 * SysInfo gathers system information when invoked (usually at startup)
 * then this information is available for error reporting and misc queries.
 * 
 * @author Kirill
 */
public class SysInfo {
	 
	/** Name of the Game */
    public final static String GameName = "CrypticShadow";
    /** Build of the Game:Version prefix (prototype, alpha, beta, final..) */
    public final static String GameVersion = "prototype";
    /** Game version in number format*/
    public final static String ReleaseNumber = "0.25"; 
    public final static String gameTitle = GameName+" "+GameVersion+" "+ReleaseNumber; 


    public static ContextCapabilities caps;
    
    public static String vendor;
    public static String renderer;
    public static String apiVer;
    public static String glslVer;
    
    public static String alVendor;
    public static String alRenderer;
    public static String alVersion;
    
    public static void querySystemInfo(){
        caps = GLContext.getCapabilities();
        
        if (AudioSystem.isCreated()){
            alVersion = AL10.alGetString(AL10.AL_VERSION);
            alRenderer = AL10.alGetString(AL10.AL_RENDERER);
            alVendor = AL10.alGetString(AL10.AL_VENDOR);
        }
        
        glslVer = GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
        apiVer = GL11.glGetString(GL11.GL_VERSION);
        renderer = GL11.glGetString(GL11.GL_RENDERER);
        vendor = GL11.glGetString(GL11.GL_VENDOR);
    }
}
