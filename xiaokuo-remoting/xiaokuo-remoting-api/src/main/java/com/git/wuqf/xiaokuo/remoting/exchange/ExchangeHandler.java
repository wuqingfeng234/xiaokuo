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
package com.git.wuqf.xiaokuo.remoting.exchange;

import com.git.wuqf.xiaokuo.remoting.ChannelHandler;
import com.git.wuqf.xiaokuo.remoting.RemotingException;
import com.git.wuqf.xiaokuo.remoting.telnet.TelnetHandler;

/**
 * ExchangeHandler. (API, Prototype, ThreadSafe)
 *
 * @author william.liangf
 */
public interface ExchangeHandler extends ChannelHandler, TelnetHandler {

    /**
     * reply.
     *
     * @param channel
     * @param request
     * @return response
     */
    Object reply(ExchangeChannel channel, Object request) throws RemotingException;

}