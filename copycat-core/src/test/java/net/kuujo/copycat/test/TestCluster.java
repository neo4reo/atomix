/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.test;

import net.kuujo.copycat.cluster.ClusterConfig;
import net.kuujo.copycat.cluster.MemberConfig;
import net.kuujo.copycat.spi.protocol.Protocol;
import net.kuujo.copycat.protocol.LocalProtocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Test cluster.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class TestCluster {
  private final List<TestNode> nodes = new ArrayList<>();

  /**
   * Adds a test node to the cluster.
   *
   * @param node The test node to add.
   * @return The test cluster.
   */
  public TestCluster addNode(TestNode node) {
    nodes.add(node);
    return this;
  }

  /**
   * Removes a test node from the cluster.
   *
   * @param node The test node to remove.
   * @return The test cluster.
   */
  public TestCluster removeNode(TestNode node) {
    nodes.remove(node);
    return this;
  }

  /**
   * Synchronously starts the test cluster.
   */
  public void start() {
    Protocol<MemberConfig> protocol = new LocalProtocol();
    for (TestNode node : nodes) {
      ClusterConfig<MemberConfig> cluster = new ClusterConfig<>();
      cluster.setLocalMember(node.config());
      for (TestNode n : nodes) {
        if (!n.id().equals(node.id())) {
          cluster.addRemoteMember(node.config());
        }
      }
      node.start(cluster, protocol);
    }
  }

}
