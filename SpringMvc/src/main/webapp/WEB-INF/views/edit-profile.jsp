<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #7f00ff, #e100ff);
        }
        .edit-profile-container {
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 400px;
        }
    </style>
</head>
<body>
    <div class="edit-profile-container text-center">
        <h2 class="mb-4">Edit Profile</h2>
        <form action="edit-profile" method="post" enctype="multipart/form-data">
            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text">👤</span>
                    <input type="text" name="username" class="form-control" value="${user.username}" readonly>
                </div>
            </div>
            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text">✉️</span>
                    <input type="email" name="email" class="form-control" value="${user.email}" required>
                </div>
            </div>
            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text">📅</span>
                    <input type="date" name="dateOfBirth" class="form-control" value="${user.dateOfBirth}" required>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Nơi ở:</label>
                <select name="provinceId" class="form-control" required>
                    <option value="">Chọn nơi ở</option>
                    <c:forEach var="province" items="${provinces}">
                        <option value="${province.idProvince}" ${province.idProvince == user.provinceId ? 'selected' : ''}>${province.nameProvince}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Avatar:</label>
                <input type="file" name="avatar" class="form-control" accept="image/jpeg,image/png">
            </div>
            <button type="submit" class="btn btn-success w-100">Save Changes</button>
        </form>
        <p class="mt-3"><a href="profile">Back to Profile</a></p>
        <p style="color:red;">${error}</p>
    </div>
</body>
</html>