/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.raft;

import net.kuujo.copycat.raft.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Abstract status context.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
abstract class RaftState implements RaftProtocol {
  protected static final byte ENTRY_TYPE_USER = 0;
  protected static final byte ENTRY_TYPE_CONFIG = 1;
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  protected final RaftContext context;
  protected ProtocolHandler<JoinRequest, JoinResponse> joinHandler;
  protected ProtocolHandler<PromoteRequest, PromoteResponse> promoteHandler;
  protected ProtocolHandler<LeaveRequest, LeaveResponse> leaveHandler;
  protected ProtocolHandler<SyncRequest, SyncResponse> syncHandler;
  protected ProtocolHandler<PollRequest, PollResponse> pollHandler;
  protected ProtocolHandler<VoteRequest, VoteResponse> voteHandler;
  protected ProtocolHandler<AppendRequest, AppendResponse> appendHandler;
  protected ProtocolHandler<CommandRequest, CommandResponse> commitHandler;
  protected ProtocolHandler<QueryRequest, QueryResponse> queryHandler;
  protected ProtocolHandler<Type, Type> transitionHandler;
  private volatile boolean open;

  protected RaftState(RaftContext context) {
    this.context = context;
  }

  /**
   * Raft status types.
   */
  public static enum Type {

    /**
     * Start status.
     */
    START(StartState.class),

    /**
     * Passive status.
     */
    PASSIVE(PassiveState.class),

    /**
     * Follower status.
     */
    FOLLOWER(FollowerState.class),

    /**
     * Candidate status.
     */
    CANDIDATE(CandidateState.class),

    /**
     * Leader status.
     */
    LEADER(LeaderState.class);

    private final Class<? extends RaftState> type;

    private Type(Class<? extends RaftState> type) {
      this.type = type;
    }

    /**
     * Returns the status type class.
     *
     * @return The status type clas.
     */
    public Class<? extends RaftState> type() {
      return type;
    }
  }

  /**
   * Returns an exceptional future with the given exception.
   */
  protected <T> CompletableFuture<T> exceptionalFuture(Throwable t) {
    CompletableFuture<T> future = new CompletableFuture<>();
    future.completeExceptionally(t);
    return future;
  }

  /**
   * Returns the Copycat status represented by this status.
   *
   * @return The Copycat status represented by this status.
   */
  public abstract Type type();

  /**
   * Logs a request.
   */
  protected final <R extends Request> R logRequest(R request) {
    LOGGER.debug("{} - Received {}", context.getLocalMember().id(), request);
    return request;
  }

  /**
   * Logs a response.
   */
  protected final <R extends Response> R logResponse(R response) {
    LOGGER.debug("{} - Sent {}", context.getLocalMember().id(), response);
    return response;
  }

  @Override
  public CompletableFuture<JoinResponse> join(JoinRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftProtocol joinHandler(ProtocolHandler<JoinRequest, JoinResponse> handler) {
    this.joinHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<PromoteResponse> promote(PromoteRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftProtocol promoteHandler(ProtocolHandler<PromoteRequest, PromoteResponse> handler) {
    this.promoteHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<LeaveResponse> leave(LeaveRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftProtocol leaveHandler(ProtocolHandler<LeaveRequest, LeaveResponse> handler) {
    this.leaveHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<SyncResponse> sync(SyncRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftProtocol syncHandler(ProtocolHandler<SyncRequest, SyncResponse> handler) {
    this.syncHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<PollResponse> poll(PollRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftProtocol pollHandler(ProtocolHandler<PollRequest, PollResponse> handler) {
    this.pollHandler = handler;
    return this;
  }

  @Override
  public RaftState voteHandler(ProtocolHandler<VoteRequest, VoteResponse> handler) {
    this.voteHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<VoteResponse> vote(VoteRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftState appendHandler(ProtocolHandler<AppendRequest, AppendResponse> handler) {
    this.appendHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<AppendResponse> append(AppendRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftProtocol queryHandler(ProtocolHandler<QueryRequest, QueryResponse> handler) {
    this.queryHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<QueryResponse> query(QueryRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  @Override
  public RaftState commandHandler(ProtocolHandler<CommandRequest, CommandResponse> handler) {
    this.commitHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<CommandResponse> command(CommandRequest request) {
    return exceptionalFuture(new IllegalStateException("Invalid Copycat state"));
  }

  /**
   * Sets a transition registerHandler on the status.
   */
  public RaftState transitionHandler(ProtocolHandler<Type, Type> handler) {
    this.transitionHandler = handler;
    return this;
  }

  @Override
  public CompletableFuture<Void> open() {
    context.checkThread();
    open = true;
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public boolean isOpen() {
    return open;
  }

  @Override
  public CompletableFuture<Void> close() {
    context.checkThread();
    open = false;
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public boolean isClosed() {
    return !open;
  }

  @Override
  public String toString() {
    return String.format("%s[context=%s]", getClass().getSimpleName(), context);
  }

}
