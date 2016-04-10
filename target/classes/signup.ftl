<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <style>
        /* Set height of the grid so .sidenav can be 100% (adjust if needed) */
        .row.content {height: 1500px}

        /* Set gray background color and 100% height */
        .sidenav {
            background-color: #f1f1f1;
            height: 100%;
        }

        /* Set black background color, white text and some padding */
        footer {
            background-color: #555;
            color: white;
            padding: 15px;
        }

        /* On small screens, set height to 'auto' for sidenav and grid */
        @media screen and (max-width: 767px) {
            .sidenav {
                height: auto;
                padding: 15px;
            }
            .row.content {height: auto;}
        }
    </style>
</head>
<body>

<div class="container-fluid">
    <div class="row content">
        <div class="col-sm-3 sidenav">
            <h4>JavaBank SA</h4>
            <ul class="nav nav-pills nav-stacked">
                <li><a href="/JavaDoc">JavaDoc</a></li>
            </ul>
        </div>

        <div class="col-sm-9">
            <h2>Create new account in JavaBank SA, or <a href ="/">login</a></h2>
            <form method="post">
                <div class="form-group">
                    <label for="username">Email:</label>
                    <input type="email" class="form-control" id="username" name="username" placeholder="Enter email" value="${username}">
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Enter password" value="">
                </div>
                <td class ="error">
                ${login_error}
                </td>

                <button type="submit" class="btn btn-default">Create new account</button>
            </form>

        </div>
    </div>
</body>
</html>