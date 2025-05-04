<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.abc.entities.*" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>

<%
com.abc.entities.User user = (com.abc.entities.User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect("login");
}
%>

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex flex-column min-vh-100 bg-light">
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
        <div class="container">
            <a class="navbar-brand" href="/">🏠 Trang chủ</a>
        </div>
        <div class="navbar-nav ms-auto d-flex align-items-center">
            <a href="logout">
                <span class="text-white me-3">🚪 Đăng xuất</span>
            </a>
        </div>
    </nav>

    <div class="container mt-5 pt-5 flex-grow-1">
        <div class="row">
            <div class="col-md-4">
                <div class="card p-3 mb-3 text-center">
                    <img src="${pageContext.request.contextPath}${user.avatar != null ? user.avatar : '/resources/images/avt.jpg'}" alt="Avatar" class="rounded-circle mx-auto" width="150">
                    <h4 class="mt-3"><%= user.getUsername() %></h4>
                    <p><strong>Email:</strong> <%= user.getEmail() != null ? user.getEmail() : "Chưa cập nhật" %></p>
                    <p><strong>Ngày sinh:</strong> <%= user.getDateOfBirth() != null ? user.getDateOfBirth() : "Chưa cập nhật" %></p>
                    <a href="edit-profile" class="btn btn-primary mt-2">Chỉnh sửa hồ sơ</a>
                </div>
            </div>
            <div class="col-md-8">
                <h3>Các bài đăng của bạn</h3>
                <%
                List<Post> posts = (List<Post>) request.getAttribute("posts");
                if (posts != null && !posts.isEmpty()) {
                    for (Post post : posts) {
                %>
                <div class="card p-3 mb-3">
                    <div class="d-flex align-items-center justify-content-between mb-2">
                        <div class="d-flex align-items-center">
                            <img src="${pageContext.request.contextPath}${user.avatar != null ? user.avatar : '/resources/images/avt.jpg'}" alt="Avatar" class="rounded-circle me-2" width="30">
                            <b><%= user.getUsername() %></b>
                            <span class="text-muted ms-3"><%= post.getCreateAt() %></span>
                        </div>
                    </div>
                    <p><strong>Trạng thái:</strong> <%= post.getStatus() %></p>
                    <p><strong><%= post.getTitle() %></strong></p>
                    <p><%= post.getBody() %></p>
                </div>
                <%
                    }
                } else {
                %>
                <p class="text-muted">Bạn chưa có bài đăng nào.</p>
                <%
                }
                %>
            </div>
        </div>
    </div>

    <footer class="bg-primary text-white text-center py-3 mt-auto">
        <div class="container">
            <p>Công ty TNHH Mạng Xã Hội Việt © 2025</p>
            <p>Ngày phát hành: 15/02/2025</p>
            <p>Bản quyền © 2025. Mọi quyền được bảo lưu.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>