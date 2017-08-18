CREATE TABLE [dbo].[schedule_tablefix](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[table_fix] [smallint] NOT NULL,
 CONSTRAINT [PK_schedule_tablefix] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_tablefix] ADD  CONSTRAINT [DF_schedule_tablefix_table_fix]  DEFAULT ((0)) FOR [table_fix]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_tablefix', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_tablefix', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务表后缀' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_tablefix', @level2type=N'COLUMN',@level2name=N'table_fix'
GO

/****** Object:  Index [schedule_tablefix_tasktype]    Script Date: 2015/11/1 11:17:08 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_tablefix_tasktype] ON [dbo].[schedule_tablefix]
(
	[task_type] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE TABLE [dbo].[schedule_task_0](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_0] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_0] ADD  CONSTRAINT [DF_schedule_task_0_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_0', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_0_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_0_fingerprint] ON [dbo].[schedule_task_0]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_0_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_0_index1] ON [dbo].[schedule_task_0]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_0_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_0_index2] ON [dbo].[schedule_task_0]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_0_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_0_select] ON [dbo].[schedule_task_0]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO


CREATE TABLE [dbo].[schedule_task_1](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_1] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_1] ADD  CONSTRAINT [DF_schedule_task_1_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_1', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_1_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_1_fingerprint] ON [dbo].[schedule_task_1]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_1_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_1_index1] ON [dbo].[schedule_task_1]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_1_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_1_index2] ON [dbo].[schedule_task_1]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_1_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_1_select] ON [dbo].[schedule_task_1]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO


CREATE TABLE [dbo].[schedule_task_2](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_2] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_2] ADD  CONSTRAINT [DF_schedule_task_2_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_2', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_2_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_2_fingerprint] ON [dbo].[schedule_task_2]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_2_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_2_index1] ON [dbo].[schedule_task_2]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_2_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_2_index2] ON [dbo].[schedule_task_2]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_2_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_2_select] ON [dbo].[schedule_task_2]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO


CREATE TABLE [dbo].[schedule_task_3](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_3] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_3] ADD  CONSTRAINT [DF_schedule_task_3_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_3', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_3_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_3_fingerprint] ON [dbo].[schedule_task_3]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_3_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_3_index1] ON [dbo].[schedule_task_3]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_3_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_3_index2] ON [dbo].[schedule_task_3]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_3_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_3_select] ON [dbo].[schedule_task_3]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE TABLE [dbo].[schedule_task_4](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_4] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_4] ADD  CONSTRAINT [DF_schedule_task_4_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_4', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_4_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_4_fingerprint] ON [dbo].[schedule_task_4]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_4_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_4_index1] ON [dbo].[schedule_task_4]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_4_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_4_index2] ON [dbo].[schedule_task_4]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_4_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_4_select] ON [dbo].[schedule_task_4]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE TABLE [dbo].[schedule_task_5](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_5] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_5] ADD  CONSTRAINT [DF_schedule_task_5_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_5', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_5_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_5_fingerprint] ON [dbo].[schedule_task_5]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_5_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_5_index1] ON [dbo].[schedule_task_5]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_5_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_5_index2] ON [dbo].[schedule_task_5]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_5_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_5_select] ON [dbo].[schedule_task_5]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE TABLE [dbo].[schedule_task_6](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_6] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_6] ADD  CONSTRAINT [DF_schedule_task_6_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_6', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_6_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_6_fingerprint] ON [dbo].[schedule_task_6]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_6_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_6_index1] ON [dbo].[schedule_task_6]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_6_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_6_index2] ON [dbo].[schedule_task_6]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_6_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_6_select] ON [dbo].[schedule_task_6]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE TABLE [dbo].[schedule_task_7](
	[r_id] [bigint] IDENTITY(1,1) NOT NULL,
	[create_time] [datetime] NOT NULL,
	[task_type] [nvarchar](255) NOT NULL,
	[task_key1] [nvarchar](255) NULL,
	[task_key2] [nvarchar](255) NULL,
	[body_class] [nvarchar](255) NOT NULL,
	[task_body] [nvarchar](max) NOT NULL,
	[status] [smallint] NOT NULL,
	[execute_count] [int] NOT NULL,
	[last_time] [datetime] NOT NULL,
	[region_no] [smallint] NULL,
	[remark] [nvarchar](255) NULL,
	[fingerprint] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_schedule_task_7] PRIMARY KEY CLUSTERED 
(
	[r_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[schedule_task_7] ADD  CONSTRAINT [DF_schedule_task_7_execute_count]  DEFAULT ((0)) FOR [execute_count]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'r_id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'create_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'task_type'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'task_key1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务关键字1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'task_key2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体类名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'body_class'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务体内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'task_body'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'status'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'execute_count'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'last_time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务分区号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'region_no'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'remark'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'任务指纹' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schedule_task_7', @level2type=N'COLUMN',@level2name=N'fingerprint'
GO

/****** Object:  Index [schedule_task_7_fingerprint]    Script Date: 2015/11/1 11:17:53 ******/
CREATE UNIQUE NONCLUSTERED INDEX [schedule_task_7_fingerprint] ON [dbo].[schedule_task_7]
(
	[fingerprint] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_7_index1]    Script Date: 2015/11/1 11:18:08 ******/
CREATE NONCLUSTERED INDEX [schedule_task_7_index1] ON [dbo].[schedule_task_7]
(
	[task_key1] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_7_index2]    Script Date: 2015/11/1 11:18:18 ******/
CREATE NONCLUSTERED INDEX [schedule_task_7_index2] ON [dbo].[schedule_task_7]
(
	[task_key2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

/****** Object:  Index [schedule_task_7_select]    Script Date: 2015/11/1 11:18:28 ******/
CREATE NONCLUSTERED INDEX [schedule_task_7_select] ON [dbo].[schedule_task_7]
(
	[status] ASC,
	[region_no] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO