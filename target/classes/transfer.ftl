
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">


    <!-- Ad Muncher content start --><script type="text/javascript" src="http://interceptedby.admuncher.com/CADC29A111E30CDF/helper.js#0.37697.0" id="YWZz_MainScript"></script><link rel="stylesheet" href="http://interceptedby.admuncher.com/CADC29A111E30CDF/helper.css" type="text/css" media="all" /><!-- Ad Muncher content end -->

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
                <li><a href="/bankOverview">Bank Overview</a></li>
                <li class="active"><a href="/myAccount">My account</a></li>
                <ul class="nav nav-pills nav-stacked">
                    <li><a href="/bankOverview">Bank Overview</a></li>
                </ul>
                <li><a href="/JavaDoc">JavaDoc</a></li>
            </ul>
        </div>

        <div class="col-sm-9">

            <div class="well well-sm"><a href="/logout" class="btn btn-info btn-sm" role="button">Logout!</a>  Welcom karol.kistela@gmail.com</div>


            <div class="panel panel-primary">
                <div class="panel-heading">Account number: ${accNR}<p><h4>saldo: ${saldo} PLN</h4></p></div>
                <div class="panel-body">

                    <ul class="nav nav-tabs">
                        <li><a href="/myAccount/deposit">deposit</a></li>
                        <li><a href="/myAccount/withdraw">withdraw</a></li>
                        <li class="active"><a href="/myAccount/transfer">transfer</a></li>
                    </ul>
                    <br>
                    <p>

                    <form method="post">
                        <div class="form-group">
                            <label for="transferTo">Transfer to:</label>
                            <input type="email" class="form-control" id="transferTo" name="transferTo" placeholder="email address" value="${transferTo}">
                            <label for="transfer">How much do you want to transfer?</label>
                            <input type="number" class="form-control" id="transfer" name="transfer" placeholder="input number" value="${transfer}">
                        </div>

                        </td>

                        <button type="submit" class="btn btn-default">transfer</button>
                    </form>

                    </p>
                </div>
            </div>
            <hr>
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
        </div>
    </div>
</body>
</html>