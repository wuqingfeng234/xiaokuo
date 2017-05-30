/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.git.wuqf.rpc.protocol.xiaokuo.telnet;


import com.git.wuqf.remoting.Channel;
import com.git.wuqf.remoting.telnet.TelnetHandler;
import com.git.wuqf.remoting.telnet.support.Help;
import com.git.wuqf.rpc.Exporter;
import com.git.wuqf.rpc.Invoker;
import com.git.wuqf.rpc.RpcContext;
import com.git.wuqf.rpc.RpcInvocation;
import com.git.wuqf.rpc.protocol.xiaokuo.XiaokuoProtocol;
import com.git.wuqf.xiaokuo.common.extension.Activate;
import com.git.wuqf.xiaokuo.common.utils.PojoUtils;
import com.git.wuqf.xiaokuo.common.utils.ReflectUtils;
import com.git.wuqf.xiaokuo.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * InvokeTelnetHandler
 * 
 * @author william.liangf
 */
@Activate
@Help(parameter = "[service.]method(args)", summary = "Invoke the service method.", detail = "Invoke the service method.")
public class InvokeTelnetHandler implements TelnetHandler {
    
    @SuppressWarnings("unchecked")
    public String telnet(Channel channel, String message) {
        if (message == null || message.length() == 0) {
            return "Please input method name, eg: \r\ninvoke xxxMethod(1234, \"abcd\", {\"prop\" : \"value\"})\r\ninvoke XxxService.xxxMethod(1234, \"abcd\", {\"prop\" : \"value\"})\r\ninvoke com.xxx.XxxService.xxxMethod(1234, \"abcd\", {\"prop\" : \"value\"})";
        }
        StringBuilder buf = new StringBuilder();
        String service = (String) channel.getAttribute(ChangeTelnetHandler.SERVICE_KEY);
        if (service != null && service.length() > 0) {
            buf.append("Use default service " + service + ".\r\n");
        }
        int i = message.indexOf("(");
        if (i < 0 || ! message.endsWith(")")) {
            return "Invalid parameters, format: service.method(args)";
        }
        String method = message.substring(0, i).trim();
        String args = message.substring(i + 1, message.length() - 1).trim();
        i = method.lastIndexOf(".");
        if (i >= 0) {
            service = method.substring(0, i).trim();
            method = method.substring(i + 1).trim();
        }
        List<Object> list;
        try {
            list = null;
        } catch (Throwable t) {
            return "Invalid json argument, cause: " + t.getMessage();
        }
        Invoker<?> invoker = null;
        Method invokeMethod = null;
        for (Exporter<?> exporter : XiaokuoProtocol.getXiaokuoProtocol().getExporters()) {
            if (service == null || service.length() == 0) {
                invokeMethod = findMethod(exporter, method, list);
                if (invokeMethod != null) {
                    invoker = exporter.getInvoker();
                    break;
                }
            } else {
                if (service.equals(exporter.getInvoker().getInterface().getSimpleName())
                        || service.equals(exporter.getInvoker().getInterface().getName())
                        || service.equals(exporter.getInvoker().getUrl().getPath())) {
                    invokeMethod = findMethod(exporter, method, list);
                    invoker = exporter.getInvoker();
                    break;
                }
            }
        }
        if (invoker != null) {
            if (invokeMethod != null) {
                try {
                    Object[] array = PojoUtils.realize(list.toArray(), invokeMethod.getParameterTypes());
                    RpcContext.getContext().setLocalAddress(channel.getLocalAddress()).setRemoteAddress(channel.getRemoteAddress());
                    long start = System.currentTimeMillis();
                    Object result = invoker.invoke(new RpcInvocation(invokeMethod, array)).recreate();
                    long end = System.currentTimeMillis();
                    buf.append("");
                    buf.append("\r\nelapsed: ");
                    buf.append(end - start);
                    buf.append(" ms.");
                } catch (Throwable t) {
                    return "Failed to invoke method " + invokeMethod.getName() + ", cause: " + StringUtils.toString(t);
                }
            } else {
                buf.append("No such method " + method + " in service " + service);
            }
        } else {
            buf.append("No such service " + service);
        }
        return buf.toString();
    }

    private static Method findMethod(Exporter<?> exporter, String method, List<Object> args) {
        Invoker<?> invoker = exporter.getInvoker();
        Method[] methods = invoker.getInterface().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(method) && isMatch(m.getParameterTypes(), args)) {
                return m;
            }
        }
        return null;
    }
    
    private static boolean isMatch(Class<?>[] types, List<Object> args) {
        if (types.length != args.size()) {
            return false;
        }
        for (int i = 0; i < types.length; i ++) {
            Class<?> type = types[i];
            Object arg = args.get(i);
            if (ReflectUtils.isPrimitive(arg.getClass())) {
                if (! ReflectUtils.isPrimitive(type)) {
                    return false;
                }
            } else if (arg instanceof Map) {
                String name = (String) ((Map<?, ?>)arg).get("class");
                Class<?> cls = arg.getClass();
                if (name != null && name.length() > 0) {
                    cls = ReflectUtils.forName(name);
                }
                if (! type.isAssignableFrom(cls)) {
                    return false;
                }
            } else if (arg instanceof Collection) {
                if (! type.isArray() && ! type.isAssignableFrom(arg.getClass())) {
                    return false;
                }
            } else {
                if (! type.isAssignableFrom(arg.getClass())) {
                    return false;
                }
            }
        }
        return true;
    }

}