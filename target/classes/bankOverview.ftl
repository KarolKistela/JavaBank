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
                <li class="active"><a href="/">Bank Overview</a></li>
                <li><a href="/myAccount">My account</a></li>
                <li><a href="/JavaDoc">JavaDoc</a></li>
            </ul>
        </div>

        <div class="col-sm-9">

            <div class="well well-sm"><a href="/logout" class="btn btn-info btn-sm" role="button">Logout!</a>  Welcom ${username}</div>

            <h3>Statistics</h3>
            <hr>

            <p><h4>Database overall statistics: </h4></p>
            <p>DataBase DNS: <b>${DNS}</b> </p>
            <p>DataBase name: <b>${DB}</b> </p>
            <p>DataBase collections:</p>
            <dl>
                <dt>Accounts</dt>
                <dd> - JSON example document: ${accountsJSON}</dd>
                <dt>Users</dt>
                <dd> - JSON example document: ${usersJSON}</dd>
                <dt>Sessions</dt>
                <dd> - JSON example document: ${sessionsJSON}</dd>
            </dl>
            <a href="/bankOverview" class="btn btn-info" role="button">Im feeling lucky</a>
            <br><br>



            <p><h4>JavaBank SA statistics: </h4></p>
            <p>Number of Users: <b>${userCount}</b></p>
            <p>Number of Accounts: <b>${accCount}</b></p>
            <p>Total cash in JavaBank SA: <b>${totalCash}</b></p>

            <h3>List of created accounts in JavaBank SA</h3>
            <hr>

                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>User's e-mail</th>
                        <th>Account NR</th>
                    </tr>
                    </thead>
                    <tbody>
                    ${HTMLlist}
                    </tbody>
                </table>

            <hr>

            <h4>Leave a Comment:</h4>
            <form role="form">
                <div class="form-group">
                    <textarea class="form-control" rows="3" required>NOT IMPLEMENTED YET</textarea>
                </div>
                <button type="submit" class="btn btn-success">Submit</button>
            </form>
            <br>

        </div>
    </div>
</body>
</html>