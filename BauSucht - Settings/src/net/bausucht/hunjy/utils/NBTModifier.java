package net.bausucht.hunjy.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class NBTModifier {
    
    public static final String PACKAGENAME = Bukkit.getServer().getClass().getPackage().getName();
    public static final String VERSION = PACKAGENAME.substring( PACKAGENAME.lastIndexOf( "." ) + 1 );
    
    public static ItemStack setNBTTag(ItemStack itemStack, String key, String value ) {
        try {
            Class<?> nmsItemStackClass = Class.forName( "net.minecraft.server." + VERSION + ".ItemStack" );
            final Class<?> craftItemStack = Class.forName( "org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack" );
            final Class<?> compountClass = Class.forName( "net.minecraft.server." + VERSION + ".NBTTagCompound" );
            final Class<?> nbtString = Class.forName( "net.minecraft.server." + VERSION + ".NBTTagString" );
            final Class<?> nbtBase = Class.forName( "net.minecraft.server." + VERSION + ".NBTBase" );
            final Method asNMSCopy = craftItemStack.getMethod( "asNMSCopy", ItemStack.class );
            final Method getTag = nmsItemStackClass.getMethod( "getTag" );
            final Object nmsItemStack = asNMSCopy.invoke( asNMSCopy, itemStack );
            Object nbtTagCompound = getTag.invoke( nmsItemStack );
            if ( nbtTagCompound == null ) {
                nbtTagCompound = compountClass.newInstance();
            }
            if ( nmsItemStack == null ) return itemStack;
            
            final Method set = nbtTagCompound.getClass().getMethod( "set", String.class, nbtBase );
            final Constructor<?> stringConstructor = nbtString.getConstructor( String.class );
            set.invoke( nbtTagCompound, key, stringConstructor.newInstance( value ) );
            final Method setTag = nmsItemStackClass.getMethod( "setTag", compountClass );
            setTag.invoke( nmsItemStack, nbtTagCompound );
            final Method asBukkitCopy = craftItemStack.getMethod( "asBukkitCopy", nmsItemStackClass );
            return (ItemStack) asBukkitCopy.invoke( asBukkitCopy, nmsItemStack );
        } catch ( ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e ) {
            e.printStackTrace();
        }
        return itemStack;
    }
    
    public static String getNBTTag( ItemStack itemStack, String key ) {
        Class<?> nmsItemStackClass = null;
        try {
            nmsItemStackClass = Class.forName( "net.minecraft.server." + VERSION + ".ItemStack" );
            final Method getTag = nmsItemStackClass.getMethod( "getTag" );
            final Class<?> craftItemStack = Class.forName( "org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack" );
            final Method asNMSCopy = craftItemStack.getMethod( "asNMSCopy", ItemStack.class );
            final Object nmsItemStack = asNMSCopy.invoke( asNMSCopy, itemStack );
            final Class<?> compountClass = Class.forName( "net.minecraft.server." + VERSION + ".NBTTagCompound" );
            Object nbtTagCompound = getTag.invoke( nmsItemStack );
            if ( nbtTagCompound == null ) {
                nbtTagCompound = compountClass.newInstance();
            }
            final Method method = compountClass.getMethod( "getString", String.class );
            return (String) method.invoke( nbtTagCompound, key );
        } catch ( ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e ) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean hasNBTTag( ItemStack itemStack, String key ) {
        try {
            Class<?> nmsItemStackClass;
            nmsItemStackClass = Class.forName( "net.minecraft.server." + VERSION + ".ItemStack" );
            final Method getTag = nmsItemStackClass.getMethod( "getTag" );
            final Class<?> craftItemStack = Class.forName( "org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack" );
            final Method asNMSCopy = craftItemStack.getMethod( "asNMSCopy", ItemStack.class );
            final Object nmsItemStack = asNMSCopy.invoke( asNMSCopy, itemStack );
            final Class<?> compountClass = Class.forName( "net.minecraft.server." + VERSION + ".NBTTagCompound" );
            Object nbtTagCompound = getTag.invoke( nmsItemStack );
            if ( nbtTagCompound == null ) {
                nbtTagCompound = compountClass.newInstance();
            }
            final Method method = compountClass.getMethod( "hasKey", String.class );
            return (Boolean) method.invoke( nbtTagCompound, key );
        } catch ( ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e ) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static ItemStack removeNBTTag( ItemStack itemStack, String key ) {
        try {
            Class<?> nmsItemStackClass = Class.forName( "net.minecraft.server." + VERSION + ".ItemStack" );
            final Class<?> craftItemStack = Class.forName( "org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack" );
            final Class<?> compountClass = Class.forName( "net.minecraft.server." + VERSION + ".NBTTagCompound" );
            final Method asNMSCopy = craftItemStack.getMethod( "asNMSCopy", ItemStack.class );
            final Method getTag = nmsItemStackClass.getMethod( "getTag" );
            final Object nmsItemStack = asNMSCopy.invoke( asNMSCopy, itemStack );
            Object nbtTagCompound = getTag.invoke( nmsItemStack );
            if ( nbtTagCompound == null ) {
                nbtTagCompound = compountClass.newInstance();
            }
            if ( nmsItemStack == null ) return itemStack;
            final Method remove = compountClass.getMethod( "remove", String.class );
            remove.invoke( nbtTagCompound, key );
            final Method setTag = nmsItemStackClass.getMethod( "setTag", compountClass );
            setTag.invoke( nmsItemStack, nbtTagCompound );
            final Method asBukkitCopy = craftItemStack.getMethod( "asBukkitCopy", nmsItemStackClass );
            return (ItemStack) asBukkitCopy.invoke( asBukkitCopy, nmsItemStack );
        } catch ( ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e ) {
            e.printStackTrace();
        }
        return itemStack;
    }
    
}
