<h1>
    ${message("welcomeToSite")}
</h1>
<p> ${message("enterUsernameAndPass")}</p>
<form class="form-horizontal login-form" role="form" name="loginForm" method="post" novalidate
      action="{{submitAction}}">
    <div class="form-group">
        <label ng-if="error">${message("wrongUsernameOrPass")}</label>
        <label ng-if="duplicateLogin">${message("duplicateLogin")}</label>
    </div>
    <div class="form-group">
        <label for="inputEmail3" class="sr-only">${message("email")}</label>
        <input type="email" name="email" id="email" class="form-control" placeholder="${message("email")}" ng-model="email"
               value="username" ng-required="true" ng-minlength="5" ng-maxlength="255">

    </div>
    <div class="form-group">
        <label for="inputPassword3" class="sr-only">${message("password")}</label>
        <input type="password" name="password" id="password" class="form-control" ng-model="password"
               placeholder="${message("password")}" value="password" ng-required="true" ng-minlength="5" ng-maxlength="255">
    </div>
    <div class="form-group">
        <label for="inputPassword3" class="sr-only">${message("rememberMe")}</label>
        <input type="checkbox" name="rememberMe" class="form-control" checked>
    </div>
    <div class="form-group" style="padding-top: 10px">

        <button type="submit" class="btn btn-primary" ng-click="submitLogin(loginForm, $event)">${message("signIn")}</button>
        <button type="submit" class="btn btn-primary" ng-click="submitRegister(loginForm, $event)">${message("register")}</button>
        <a href="/reset-password">${message("resetPassword.forgotPassword")}</a

    </div>

    <div class="main-example">
        <div class="main-example-image"><img src="/assets/img/main-photos/1.jpg"></div>
        <div class="main-example-text">${message("bio.1")}</div>
    </div>
    <div class="main-example">
        <div class="main-example-image"><img src="/assets/img/main-photos/2.jpg"></div>
        <div class="main-example-text">${message("bio.2")}</div>
    </div>
    <div class="main-example">
        <div class="main-example-image"><img src="/assets/img/main-photos/3.jpg"></div>
        <div class="main-example-text">${message("bio.3")}</div>
    </div>
</form>

