USE [master]
GO
/****** Object:  Database [QLBanDienThoai]    Script Date: 07/08/2024 10:22:29 CH ******/
CREATE DATABASE [QLBanDienThoai]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'QLBanDienThoai', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\QLBanDienThoai.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'QLBanDienThoai_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\QLBanDienThoai_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [QLBanDienThoai] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QLBanDienThoai].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QLBanDienThoai] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET ARITHABORT OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QLBanDienThoai] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QLBanDienThoai] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET  DISABLE_BROKER 
GO
ALTER DATABASE [QLBanDienThoai] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QLBanDienThoai] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [QLBanDienThoai] SET  MULTI_USER 
GO
ALTER DATABASE [QLBanDienThoai] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QLBanDienThoai] SET DB_CHAINING OFF 
GO
ALTER DATABASE [QLBanDienThoai] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [QLBanDienThoai] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [QLBanDienThoai] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [QLBanDienThoai] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'QLBanDienThoai', N'ON'
GO
ALTER DATABASE [QLBanDienThoai] SET QUERY_STORE = OFF
GO
USE [QLBanDienThoai]
GO
/****** Object:  Table [dbo].[ChiTietHoaDon]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietHoaDon](
	[MaCTHD] [int] IDENTITY(1,1) NOT NULL,
	[MaHD] [int] NOT NULL,
	[MaSP] [nvarchar](10) NOT NULL,
	[GiaSP] [float] NOT NULL,
	[SoLuong] [int] NOT NULL,
 CONSTRAINT [PK__ChiTietH__1E4FA7716FEC2D9D] PRIMARY KEY CLUSTERED 
(
	[MaCTHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HangSanPham]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HangSanPham](
	[MaHang] [int] IDENTITY(1,1) NOT NULL,
	[TenHang] [nvarchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[MaHang] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HoaDon]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDon](
	[MaHD] [int] IDENTITY(1,1) NOT NULL,
	[NgayNhap] [date] NULL,
	[NgayBan] [date] NULL,
	[Loai] [bit] NOT NULL,
	[GiamGia] [int] NULL,
	[MaNV] [nchar](10) NULL,
	[MaKH] [nchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[MaHD] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KhachHang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhachHang](
	[SDT] [nchar](10) NOT NULL,
	[HoTen] [nvarchar](50) NOT NULL,
	[DiaChi] [nvarchar](100) NOT NULL,
	[SoLanMua] [int] NULL,
	[NgayTao] [date] NULL,
PRIMARY KEY CLUSTERED 
(
	[SDT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NhanVien]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhanVien](
	[SDT] [nchar](10) NOT NULL,
	[HoTen] [nvarchar](50) NOT NULL,
	[MatKhau] [nchar](10) NOT NULL,
	[GioiTinh] [bit] NOT NULL,
	[DiaChi] [nvarchar](100) NOT NULL,
	[VaiTro] [bit] NOT NULL,
	[TrangThai] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[SDT] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SanPham]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SanPham](
	[MaSP] [nvarchar](10) NOT NULL,
	[TenSP] [nvarchar](50) NOT NULL,
	[GiaNhap] [float] NULL,
	[GiaBan] [float] NULL,
	[SoLuong] [int] NULL,
	[MaHang] [int] NOT NULL,
	[MoTa] [nvarchar](200) NULL,
	[NgayNhap] [date] NULL,
	[TrangThai] [bit] NULL,
 CONSTRAINT [PK__SanPham__2725081C17D9FCEA] PRIMARY KEY CLUSTERED 
(
	[MaSP] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[ChiTietHoaDon] ON 

INSERT [dbo].[ChiTietHoaDon] ([MaCTHD], [MaHD], [MaSP], [GiaSP], [SoLuong]) VALUES (7, 8, N'SP002', 100, 2)
INSERT [dbo].[ChiTietHoaDon] ([MaCTHD], [MaHD], [MaSP], [GiaSP], [SoLuong]) VALUES (8, 1, N'SP002', 3000000, 5)
INSERT [dbo].[ChiTietHoaDon] ([MaCTHD], [MaHD], [MaSP], [GiaSP], [SoLuong]) VALUES (9, 2, N'SP002', 3000000, 456)
INSERT [dbo].[ChiTietHoaDon] ([MaCTHD], [MaHD], [MaSP], [GiaSP], [SoLuong]) VALUES (10, 3, N'SP002', 7500000, 10)
INSERT [dbo].[ChiTietHoaDon] ([MaCTHD], [MaHD], [MaSP], [GiaSP], [SoLuong]) VALUES (11, 4, N'SP004', 40000000, 100)
INSERT [dbo].[ChiTietHoaDon] ([MaCTHD], [MaHD], [MaSP], [GiaSP], [SoLuong]) VALUES (12, 5, N'SP004', 0, 1)
INSERT [dbo].[ChiTietHoaDon] ([MaCTHD], [MaHD], [MaSP], [GiaSP], [SoLuong]) VALUES (13, 6, N'SP004', 111, 49)
SET IDENTITY_INSERT [dbo].[ChiTietHoaDon] OFF
GO
SET IDENTITY_INSERT [dbo].[HangSanPham] ON 

INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (1, N'Oppo')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (2, N'IPhone')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (3, N'Xiaomi')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (4, N'Samsung')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (6, N'OnePlus')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (7, N'readmi')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (9, N'SE#@#')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (10, N'32')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (13, N'dfvfw45t34524634')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (14, N'dfvfw45t34524634@!#')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (16, N'12345678901234567890')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (17, N'')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (18, N'sdcsdccsd')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (19, N'')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (20, N'')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (21, N'Hiếu Nguyên')
INSERT [dbo].[HangSanPham] ([MaHang], [TenHang]) VALUES (22, N'Hiếu Nguyên 1')
SET IDENTITY_INSERT [dbo].[HangSanPham] OFF
GO
SET IDENTITY_INSERT [dbo].[HoaDon] ON 

INSERT [dbo].[HoaDon] ([MaHD], [NgayNhap], [NgayBan], [Loai], [GiamGia], [MaNV], [MaKH]) VALUES (1, CAST(N'2024-02-27' AS Date), NULL, 0, 0, N'0989953290', NULL)
INSERT [dbo].[HoaDon] ([MaHD], [NgayNhap], [NgayBan], [Loai], [GiamGia], [MaNV], [MaKH]) VALUES (2, CAST(N'2024-02-27' AS Date), NULL, 0, 0, N'0989953290', NULL)
INSERT [dbo].[HoaDon] ([MaHD], [NgayNhap], [NgayBan], [Loai], [GiamGia], [MaNV], [MaKH]) VALUES (3, NULL, CAST(N'2024-02-28' AS Date), 1, 0, N'0398250140', N'0989953547')
INSERT [dbo].[HoaDon] ([MaHD], [NgayNhap], [NgayBan], [Loai], [GiamGia], [MaNV], [MaKH]) VALUES (4, CAST(N'2024-02-28' AS Date), NULL, 0, 0, N'0989953290', NULL)
INSERT [dbo].[HoaDon] ([MaHD], [NgayNhap], [NgayBan], [Loai], [GiamGia], [MaNV], [MaKH]) VALUES (5, CAST(N'2024-02-28' AS Date), NULL, 0, 0, N'0989953290', NULL)
INSERT [dbo].[HoaDon] ([MaHD], [NgayNhap], [NgayBan], [Loai], [GiamGia], [MaNV], [MaKH]) VALUES (6, CAST(N'2024-07-21' AS Date), NULL, 0, 0, N'0989953290', NULL)
SET IDENTITY_INSERT [dbo].[HoaDon] OFF
GO
INSERT [dbo].[KhachHang] ([SDT], [HoTen], [DiaChi], [SoLanMua], [NgayTao]) VALUES (N'0112266554', N'Ngô Văn A', N'137 Tô Hiệu', 5, CAST(N'2020-11-18' AS Date))
INSERT [dbo].[KhachHang] ([SDT], [HoTen], [DiaChi], [SoLanMua], [NgayTao]) VALUES (N'0223345556', N'Ngô Văn A', N'137 Tô Hiệu', 2, CAST(N'2020-11-18' AS Date))
INSERT [dbo].[KhachHang] ([SDT], [HoTen], [DiaChi], [SoLanMua], [NgayTao]) VALUES (N'0989953547', N'Nguyễn Văn Nam', N'Cần Thơ', 1, CAST(N'2024-02-28' AS Date))
GO
INSERT [dbo].[NhanVien] ([SDT], [HoTen], [MatKhau], [GioiTinh], [DiaChi], [VaiTro], [TrangThai]) VALUES (N'0398250138', N'Nguyễn Tuấn Khôi', N'1234567890', 0, N'Đà Nẵng', 0, 1)
INSERT [dbo].[NhanVien] ([SDT], [HoTen], [MatKhau], [GioiTinh], [DiaChi], [VaiTro], [TrangThai]) VALUES (N'0398250140', N'Khải', N'1234567890', 1, N'Đà Nẵng', 1, 1)
INSERT [dbo].[NhanVien] ([SDT], [HoTen], [MatKhau], [GioiTinh], [DiaChi], [VaiTro], [TrangThai]) VALUES (N'0888888888', N'nguyen', N'8888888888', 1, N'', 0, 0)
INSERT [dbo].[NhanVien] ([SDT], [HoTen], [MatKhau], [GioiTinh], [DiaChi], [VaiTro], [TrangThai]) VALUES (N'0987265142', N'Tống Bình Phú', N'1234567890', 1, N'', 0, 1)
INSERT [dbo].[NhanVien] ([SDT], [HoTen], [MatKhau], [GioiTinh], [DiaChi], [VaiTro], [TrangThai]) VALUES (N'0989953290', N'Đỗ Hiếu Nguyên', N'1234567890', 1, N'Đà Nẵng', 1, 1)
GO
INSERT [dbo].[SanPham] ([MaSP], [TenSP], [GiaNhap], [GiaBan], [SoLuong], [MaHang], [MoTa], [NgayNhap], [TrangThai]) VALUES (N'SP001', N'Oppo A36', 2500000, 12000000, 14, 1, N'Oppo a36', CAST(N'2020-11-19' AS Date), 1)
INSERT [dbo].[SanPham] ([MaSP], [TenSP], [GiaNhap], [GiaBan], [SoLuong], [MaHang], [MoTa], [NgayNhap], [TrangThai]) VALUES (N'SP002', N'Redmi note8 32GB', 3000000, 7500000, 466, 3, N'maksmdkmk', CAST(N'2024-02-27' AS Date), 1)
INSERT [dbo].[SanPham] ([MaSP], [TenSP], [GiaNhap], [GiaBan], [SoLuong], [MaHang], [MoTa], [NgayNhap], [TrangThai]) VALUES (N'SP003', N'Asd453@!!#@', 0, 10000, 0, 1, N'', NULL, 1)
INSERT [dbo].[SanPham] ([MaSP], [TenSP], [GiaNhap], [GiaBan], [SoLuong], [MaHang], [MoTa], [NgayNhap], [TrangThai]) VALUES (N'SP004', N'iphone 15 pro max', 111, 30000000, 150, 1, N'Màu ĐEN', CAST(N'2024-07-21' AS Date), 1)
INSERT [dbo].[SanPham] ([MaSP], [TenSP], [GiaNhap], [GiaBan], [SoLuong], [MaHang], [MoTa], [NgayNhap], [TrangThai]) VALUES (N'SP005', N'sdc', 0, 0, 0, 1, N'', NULL, 1)
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [IX_SanPham]    Script Date: 07/08/2024 10:22:30 CH ******/
ALTER TABLE [dbo].[SanPham] ADD  CONSTRAINT [IX_SanPham] UNIQUE NONCLUSTERED 
(
	[TenSP] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[HoaDon] ADD  DEFAULT ((0)) FOR [GiamGia]
GO
ALTER TABLE [dbo].[KhachHang] ADD  DEFAULT ((1)) FOR [SoLanMua]
GO
ALTER TABLE [dbo].[KhachHang] ADD  DEFAULT (getdate()) FOR [NgayTao]
GO
ALTER TABLE [dbo].[NhanVien] ADD  DEFAULT ((1)) FOR [TrangThai]
GO
ALTER TABLE [dbo].[SanPham] ADD  CONSTRAINT [DF__SanPham__GiaNhap__4AB81AF0]  DEFAULT ((0)) FOR [GiaNhap]
GO
ALTER TABLE [dbo].[SanPham] ADD  CONSTRAINT [DF__SanPham__GiaBan__4BAC3F29]  DEFAULT ((0)) FOR [GiaBan]
GO
ALTER TABLE [dbo].[SanPham] ADD  CONSTRAINT [DF__SanPham__SoLuong__4CA06362]  DEFAULT ((0)) FOR [SoLuong]
GO
ALTER TABLE [dbo].[SanPham] ADD  CONSTRAINT [DF__SanPham__NgayNha__4E88ABD4]  DEFAULT (getdate()) FOR [NgayNhap]
GO
ALTER TABLE [dbo].[SanPham] ADD  CONSTRAINT [DF__SanPham__TrangTh__4F7CD00D]  DEFAULT ((1)) FOR [TrangThai]
GO
ALTER TABLE [dbo].[ChiTietHoaDon]  WITH CHECK ADD  CONSTRAINT [FK__ChiTietHoa__MaSP__5812160E] FOREIGN KEY([MaSP])
REFERENCES [dbo].[SanPham] ([MaSP])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ChiTietHoaDon] CHECK CONSTRAINT [FK__ChiTietHoa__MaSP__5812160E]
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([MaKH])
REFERENCES [dbo].[KhachHang] ([SDT])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([MaNV])
REFERENCES [dbo].[NhanVien] ([SDT])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SanPham]  WITH CHECK ADD  CONSTRAINT [FK__SanPham__MaHang__4D94879B] FOREIGN KEY([MaHang])
REFERENCES [dbo].[HangSanPham] ([MaHang])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SanPham] CHECK CONSTRAINT [FK__SanPham__MaHang__4D94879B]
GO
/****** Object:  StoredProcedure [dbo].[banHang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[banHang]
	@maSP nvarchar(10),
	@soLuong int,
	@gia float,
	@maKH nchar(10)
as
begin
	Declare @maHD int = (select max(MaHD) from HoaDon where MaKH = @maKH);
	insert into ChiTietHoaDon(MaHD, MaSP, GiaSP, SoLuong) values (@maHD, @maSP, @gia, @soLuong);
	update SanPham set SoLuong = SoLuong - @soLuong where MaSP = @maSP;
end
GO
/****** Object:  StoredProcedure [dbo].[doanhThu]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[doanhThu]
as
select
	dtbr.BanRa - dtnv.NhapVao as DoanhThu
from (
	select
		SUM(dtthd.NhapVao) as NhapVao
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as NhapVao
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.Loai = 0
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd
) as dtnv,
(
	select
		SUM(dtthd.BanRa) as BanRa
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as BanRa
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.Loai = 1
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd
) as dtbr;
GO
/****** Object:  StoredProcedure [dbo].[doanhThuTheoNgay]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[doanhThuTheoNgay]
	@ngay date
as
select
	dtbr.BanRa - dtnv.NhapVao as DoanhThu
from (
	select
		SUM(dtthd.NhapVao) as NhapVao
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as NhapVao
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.NgayNhap = @ngay
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd
) as dtnv,
(
	select
		SUM(dtthd.BanRa) as BanRa
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as BanRa
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.NgayBan = @ngay
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd
) as dtbr;
GO
/****** Object:  StoredProcedure [dbo].[doanhThuTheoThang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[doanhThuTheoThang]
as
begin
	CREATE TABLE #doanhThuTheoThang (
		Thang int,
		SoTien float
	);
	Declare @thang int = 1;
	while @thang <= 12
	begin
		INSERT INTO #doanhThuTheoThang
		select
			@thang,
			dtbr.BanRa - dtnv.NhapVao
		from (
			select
				SUM(dtthd.NhapVao) as NhapVao
			from (
				select
					SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as NhapVao
				from HoaDon as hd
				join ChiTietHoaDon as cthd
					on cthd.MaHD = hd.MaHD 
				WHERE 
					MONTH(hd.NgayNhap) = @thang AND
					YEAR(hd.NgayNhap) = YEAR(GETDATE())
				GROUP BY hd.MaHD, hd.GiamGia
			) as dtthd
		) as dtnv,
		(
			select
				SUM(dtthd.BanRa) as BanRa
			from (
				select
					SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as BanRa
				from HoaDon as hd
				join ChiTietHoaDon as cthd
					on cthd.MaHD = hd.MaHD 
				WHERE 
					MONTH(hd.NgayBan) = @thang AND
					YEAR(hd.NgayBan) = YEAR(GETDATE())
				GROUP BY hd.MaHD, hd.GiamGia
			) as dtthd
		) as dtbr;
		set @thang = @thang + 1;
	end

	select * from #doanhThuTheoThang;
	drop table #doanhThuTheoThang;
end
GO
/****** Object:  StoredProcedure [dbo].[KhachHangTheoThang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[KhachHangTheoThang]
as
begin
	CREATE TABLE #KhachHangTheoThang(
		Thang int,
		SoLuong int
	);
	Declare @thang int = 1;
	while @thang <= 12
	begin
		INSERT INTO #KhachHangTheoThang
		select
			@thang,
			SUM(dtthd.KhachHang) as KhachHang
		from (
			select COUNT(*) as KhachHang from HoaDon hd 
			where
				MONTH(hd.NgayBan) = @thang AND
				YEAR(hd.NgayBan) = YEAR(GETDATE())
			
		) as dtthd;
		set @thang = @thang + 1;
	end

	select * from #KhachHangTheoThang;
	drop table #KhachHangTheoThang;
end
GO
/****** Object:  StoredProcedure [dbo].[nhapHang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[nhapHang]
	@maSP nvarchar(10),
	@giaNhap float,
	@giaBan float,
	@soLuong int,
	@ngayNhap date
as
begin
	Declare @maHD int = (select max(MaHD) from HoaDon where Loai = 0);
	insert into ChiTietHoaDon(MaHD, MaSP, GiaSP, SoLuong) values (@maHD, @maSP, @giaNhap, @soLuong);
	update SanPham set GiaNhap = @giaNhap, GiaBan = @giaBan, SoLuong = SoLuong + @soLuong, NgayNhap = @ngayNhap where MaSP = @maSP;
end
GO
/****** Object:  StoredProcedure [dbo].[soTienBanRa]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[soTienBanRa]
as
begin

	select
		SUM(dtthd.BanRa) as BanRa
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as BanRa
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.Loai = 1
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd;
	
end;
GO
/****** Object:  StoredProcedure [dbo].[soTienBanRaTheoNgay]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[soTienBanRaTheoNgay]
	@ngay date
as
begin

	select
		SUM(dtthd.BanRa) as BanRa
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as BanRa
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.NgayBan = @ngay
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd;
	
end;
GO
/****** Object:  StoredProcedure [dbo].[soTienBanRaTheoThang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[soTienBanRaTheoThang]
as
begin
	CREATE TABLE #doanhThuBanRaTheoThang (
		Thang int,
		SoTien float
	);
	Declare @thang int = 1;
	while @thang <= 12
	begin
		INSERT INTO #doanhThuBanRaTheoThang
		select
			@thang,
			SUM(dtthd.BanRa) as BanRa
		from (
			select
				SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as BanRa
			from HoaDon as hd
			join ChiTietHoaDon as cthd
				on cthd.MaHD = hd.MaHD 
			WHERE 
				MONTH(hd.NgayBan) = @thang AND
				YEAR(hd.NgayBan) = YEAR(GETDATE())
			GROUP BY hd.MaHD, hd.GiamGia
		) as dtthd;
		set @thang = @thang + 1;
	end

	select * from #doanhThuBanRaTheoThang;
	drop table #doanhThuBanRaTheoThang;
end
GO
/****** Object:  StoredProcedure [dbo].[soTienNhapVao]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[soTienNhapVao]
as
begin

	select
		SUM(dtthd.NhapVao) as NhapVao
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as NhapVao
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.Loai = 0
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd;
	
end;
GO
/****** Object:  StoredProcedure [dbo].[soTienNhapVaoTheoNgay]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[soTienNhapVaoTheoNgay]
	@ngay date
as
begin

	select
		SUM(dtthd.NhapVao) as NhapVao
	from (
		select
			SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as NhapVao
		from HoaDon as hd
		join ChiTietHoaDon as cthd
			on cthd.MaHD = hd.MaHD 
		WHERE 
			hd.NgayNhap = @ngay
		GROUP BY hd.MaHD, hd.GiamGia
	) as dtthd;
	
end;
GO
/****** Object:  StoredProcedure [dbo].[soTienNhapVaoTheoThang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[soTienNhapVaoTheoThang]
as
begin
	CREATE TABLE #soTienNhapVaoTheoThang (
		Thang int,
		SoTien float
	);
	Declare @thang int = 1;
	while @thang <= 12
	begin
		INSERT INTO #soTienNhapVaoTheoThang
		select
			@thang,
			SUM(dtthd.BanRa) as BanRa
		from (
			select
				SUM(cthd.SoLuong * cthd.GiaSP) - hd.GiamGia * SUM(cthd.SoLuong * cthd.GiaSP)/100 as BanRa
			from HoaDon as hd
			join ChiTietHoaDon as cthd
				on cthd.MaHD = hd.MaHD 
			WHERE 
				MONTH(hd.NgayNhap) = @thang AND
				YEAR(hd.NgayNhap) = YEAR(GETDATE())
			GROUP BY hd.MaHD, hd.GiamGia
		) as dtthd;
		set @thang = @thang + 1;
	end

	select * from #soTienNhapVaoTheoThang;
	drop table #soTienNhapVaoTheoThang;
end
GO
/****** Object:  StoredProcedure [dbo].[sp_soLuong]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[sp_soLuong]
as
begin
	Declare @soLuongNhanVien int = (SELECT COUNT(*) as SoLuong FROM NhanVien);
	Declare @soLuongHangSX int = (SELECT COUNT(*) from HangSanPham);
	Declare @soLuongSPTrongKho int = (SELECT SUM(SoLuong) as SoLuong from SanPham)

	SELECT @soLuongNhanVien as NhanVien, @soLuongHangSX as HangSX, @soLuongSPTrongKho as TrongKho
end
GO
/****** Object:  StoredProcedure [dbo].[sp_soLuongKhachHang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[sp_soLuongKhachHang]
as
begin
	Declare @soLuongKhachHang int = (select COUNT(*) as SoLuong from KhachHang kh);
	Declare @SoLuongKhachHomNay int = (select COUNT(*) as SoLuong from HoaDon hd where DAY(hd.NgayBan) = DAY(GETDATE()));
	select @soLuongKhachHang as SoLuongKhach, @SoLuongKhachHomNay as HomNay
end
GO
/****** Object:  StoredProcedure [dbo].[sp_SpBanDuoc]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[sp_SpBanDuoc]
as
begin
	Declare @soLuongBan int = (select SUM(ct.SoLuong) from HoaDon hd join ChiTietHoaDon ct on hd.MaHD = ct.MaHD where hd.Loai=1);
	Declare @soLuongBanHomNay int = (select SUM(ct.SoLuong) from HoaDon hd join ChiTietHoaDon ct on hd.MaHD = ct.MaHD
										where DAY(hd.NgayBan)= DAY(GETDATE()))
	select @soLuongBan as SoLuongBan, ISNULL(@soLuongBanHomNay, 0) as HomNay
end
GO
/****** Object:  StoredProcedure [dbo].[sp_SpNhapVao]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[sp_SpNhapVao]
as
begin
	Declare @soLuongNhap int = (select SUM(ct.SoLuong) from HoaDon hd join ChiTietHoaDon ct on hd.MaHD = ct.MaHD where hd.Loai=0);
	Declare @soLuongNhapHomNay int = (select SUM(ct.SoLuong) from HoaDon hd join ChiTietHoaDon ct on hd.MaHD = ct.MaHD
										where DAY(hd.NgayNhap)= DAY(GETDATE()))
	select @soLuongNhap as SoLuongNhap, ISNULL(@soLuongNhapHomNay, 0) as HomNay
end
GO
/****** Object:  StoredProcedure [dbo].[SpBanRaTheoThang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[SpBanRaTheoThang]
as
begin
	CREATE TABLE #SpNhapVaoTheoThang(
		Thang int,
		SoLuong int
	);
	Declare @thang int = 1;
	while @thang <= 12
	begin
		INSERT INTO #SpNhapVaoTheoThang
		select
			@thang,
			ISNULL(SUM(dtthd.BanRa),0) as BanRa
		from (
			select SUM(ct.SoLuong) as BanRa from HoaDon hd join ChiTietHoaDon ct on hd.MaHD = ct.MaHD
			where
				MONTH(hd.NgayBan) = @thang AND
				YEAR(hd.NgayBan) = YEAR(GETDATE())
			
		) as dtthd;
		set @thang = @thang + 1;
	end

	select * from #SpNhapVaoTheoThang;
	drop table #SpNhapVaoTheoThang;
end
GO
/****** Object:  StoredProcedure [dbo].[SpNhapVaoTheoThang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[SpNhapVaoTheoThang]
as
begin
	CREATE TABLE #SpNhapVaoTheoThang(
		Thang int,
		SoLuong int
	);
	Declare @thang int = 1;
	while @thang <= 12
	begin
		INSERT INTO #SpNhapVaoTheoThang
		select
			@thang,
			ISNULL(SUM(dtthd.NhapVao),0) as NhapVao
		from (
			select SUM(ct.SoLuong) as NhapVao from HoaDon hd join ChiTietHoaDon ct on hd.MaHD = ct.MaHD
			where
				MONTH(hd.NgayNhap) = @thang AND
				YEAR(hd.NgayNhap) = YEAR(GETDATE())
			
		) as dtthd;
		set @thang = @thang + 1;
	end

	select * from #SpNhapVaoTheoThang;
	drop table #SpNhapVaoTheoThang;
end
GO
/****** Object:  StoredProcedure [dbo].[themKhachHang]    Script Date: 07/08/2024 10:22:30 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[themKhachHang]
	@SDT nchar(10),
	@HoTen nvarchar(50),
	@DiaChi nvarchar(100),
	@ngayTao date
as
begin
	if exists (Select * from KhachHang where SDT = @SDT and DiaChi = @DiaChi)
		begin
			update KhachHang set SoLanMua = SoLanMua + 1 where SDT = @SDT;
		end
	else if exists (Select * from KhachHang where SDT = @SDT and DiaChi <> @DiaChi)
		begin
			update KhachHang set DiaChi = @DiaChi, SoLanMua = SoLanMua + 1 where SDT = @SDT;
		end
	else
		begin
			insert into KhachHang values(@SDT, @HoTen, @DiaChi, 1, @ngayTao);
		end
end
GO
USE [master]
GO
ALTER DATABASE [QLBanDienThoai] SET  READ_WRITE 
GO
