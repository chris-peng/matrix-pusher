package top.lcmatrix.matrixpusher.server.client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

public class ClientInfo {

	private Set<String> userIds = new HashSet<>();
	private Channel channel;
	private HeartbeatTask heartbeatTask;
	
	public ClientInfo(String userId, Channel channel, HeartbeatTask heartbeatTask) {
		this.userIds.add(userId);
		this.channel = channel;
		this.heartbeatTask = heartbeatTask;
	}
	
	public Set<String> getUserIds() {
		return Collections.unmodifiableSet(userIds);
	}
	public void addUserId(String userId) {
		this.userIds.add(userId);
	}
	public void removeUserId(String userId) {
		this.userIds.remove(userId);
	}
	public int userIdCount() {
		return this.userIds.size();
	}
	public Channel getChannel() {
		return channel;
	}
	public HeartbeatTask getHeartbeatTask() {
		return heartbeatTask;
	}

	public void setHeartbeatTask(HeartbeatTask heartbeatTask) {
		this.heartbeatTask = heartbeatTask;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public static class HeartbeatTask{
		private ScheduledFuture<?> scheduler;
		private Runnable task;
		
		public HeartbeatTask() {}
		public HeartbeatTask(ScheduledFuture<?> scheduler, Runnable task) {
			super();
			this.scheduler = scheduler;
			this.task = task;
		}
		public ScheduledFuture<?> getScheduler() {
			return scheduler;
		}
		public void setScheduler(ScheduledFuture<?> scheduler) {
			this.scheduler = scheduler;
		}
		public Runnable getTask() {
			return task;
		}
		public void setTask(Runnable task) {
			this.task = task;
		}
	}
}
