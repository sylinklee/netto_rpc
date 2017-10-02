package com.netto.schedule.support;

import com.netto.schedule.IScheduleTaskProcess;
import com.netto.schedule.ScheduleParam;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class AbstractScheduleTaskProcess<T> implements IScheduleTaskProcess {

	protected final static Logger logger = Logger.getLogger(AbstractScheduleTaskProcess.class);

	private ExecutorService executor;

	private volatile int lastThreadCount = 0;

	public int execute(ScheduleParam param, Integer curServer) {
		// if (logger.isInfoEnabled()) {
		// logger.info(
		// "开始获取任务[" + this.getClass().getName() + "][" + curServer + "/" +
		// param.getServerCount() + "]....");
		// }

		List<T> tasks = this.selectTasks(param, curServer);

		if (logger.isInfoEnabled()) {
			logger.info("获取任务[" + this.getClass().getName() + "][" + curServer + "/" + param.getInovkerCount() + "]共"
					+ (tasks == null ? 0 : tasks.size()) + "条");
		}
		if (tasks == null) {
			return 0;
		}
		// if (logger.isInfoEnabled()) {
		// logger.info(
		// "开始执行任务[" + this.getClass().getName() + "][" + curServer + "/" +
		// param.getServerCount() + "]....");
		// }
		this.innerExecuteTasks(param, tasks);
		if (logger.isInfoEnabled()) {
			logger.info("执行任务[" + this.getClass().getName() + "][" + curServer + "/" + param.getInovkerCount() + "]共"
					+ tasks.size() + "条完成!");
		}
		return tasks.size();
	}

	protected abstract List<T> selectTasks(ScheduleParam param, Integer curServer);

	protected abstract void executeTasks(ScheduleParam param, List<T> tasks);

	private void innerExecuteTasks(final ScheduleParam param, List<T> tasks) {
		if (this.executor == null) {
			this.executor = createCustomExecutorService(param.getClientThreadCount(), "dts.executeTasks");
			this.lastThreadCount = param.getClientThreadCount();
		} else {
			if (this.lastThreadCount != param.getClientThreadCount()) {
				this.executor.shutdown();
				this.executor = createCustomExecutorService(param.getClientThreadCount(), "dts.executeTasks");
				this.lastThreadCount = param.getClientThreadCount();
			}
		}
		List<List<T>> lists = splitList(tasks, param.getExecuteCount());
		final CountDownLatch latch = new CountDownLatch(lists.size());
		for (final List<T> list : lists) {
			this.executor.submit(new Callable<Object>() {
				public Object call() throws Exception {
					try {
						// if (logger.isInfoEnabled()) {
						// logger.info("正在执行任务[" + this.getClass().getName() +
						// "],条数:" + list.size() + "...");
						// }
						executeTasks(param, list);
						// if (logger.isInfoEnabled()) {
						// logger.info("执行任务[" + this.getClass().getName() +
						// "],条数:" + list.size() + "成功!");
						// }
						return null;
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						throw e;
					} finally {
						latch.countDown();
					}
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException("interrupted when processing data access request in concurrency", e);
		}
	}

	private static ExecutorService createCustomExecutorService(int poolSize, final String method) {
		int coreSize = poolSize;
		ThreadFactory tf = new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r, "custom thread:" + method);
				return t;
			}
		};
		BlockingQueue<Runnable> queueToUse = new LinkedBlockingQueue<Runnable>();
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, poolSize, 60, TimeUnit.SECONDS, queueToUse,
				tf, new ThreadPoolExecutor.CallerRunsPolicy());
		// 1. register executor for disposing explicitly
		// internalExecutorServiceRegistry.add(executor);
		// 2. dispose executor implicitly
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					executor.shutdown();
					executor.awaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					logger.warn("interrupted when shuting down the query executor:\n{}", e);
				}
			}
		});
		return executor;
	}

	private static <T> List<List<T>> splitList(List<T> tasks, int maxLen) {
		if (maxLen <= 0) {
			throw new RuntimeException("maxLen 不能小于等于0!");
		}
		List<List<T>> result = new ArrayList<List<T>>();
		int count = 0;
		if (tasks.size() % maxLen == 0) {
			count = tasks.size() / maxLen;
		} else {
			count = tasks.size() / maxLen + 1;
		}
		for (int i = 0; i < count; i++) {
			int fromIndex = i * maxLen;
			int toIndex = 0;
			if (i == count - 1) {
				toIndex = tasks.size();
			} else {
				toIndex = (i + 1) * maxLen;
			}
			result.add(tasks.subList(fromIndex, toIndex));
		}
		return result;
	}
}
