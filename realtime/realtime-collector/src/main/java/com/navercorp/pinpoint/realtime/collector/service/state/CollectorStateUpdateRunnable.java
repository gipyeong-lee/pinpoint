/*
 * Copyright 2023 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.realtime.collector.service.state;

import com.navercorp.pinpoint.realtime.collector.receiver.ClusterPointLocator;
import com.navercorp.pinpoint.realtime.vo.CollectorState;
import com.navercorp.pinpoint.realtime.vo.ProfilerDescription;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author youngjin.kim2
 */
public class CollectorStateUpdateRunnable implements Runnable {

    private final ClusterPointLocator clusterPointLocator;
    private final CollectorStatePublisherService dao;

    public CollectorStateUpdateRunnable(ClusterPointLocator clusterPointLocator, CollectorStatePublisherService dao) {
        this.clusterPointLocator = Objects.requireNonNull(clusterPointLocator, "clusterPointLocator");
        this.dao = Objects.requireNonNull(dao, "dao");
    }

    @Override
    public void run() {
        dao.publish(getCollectorState());
    }

    private CollectorState getCollectorState() {
        List<ProfilerDescription> clusterKeys = this.clusterPointLocator.getClusterPointList().stream()
                .map(point -> new ProfilerDescription(point.getClusterKey()))
                .collect(Collectors.toUnmodifiableList());
        return new CollectorState(clusterKeys);
    }

}
