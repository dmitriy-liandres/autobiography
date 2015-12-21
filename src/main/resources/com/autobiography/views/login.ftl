<h1>
    ${message("welcomeToSite")}
</h1>
<p> ${message("enterUsernameAndPass")}</p>
<form class="form-horizontal login-form" role="form" name="loginForm" method="post" novalidate
      action="{{submitAction}}">
    <div class="form-group">
        <label ng-if="error">${message("wrongUsernameOrPass")}</label>
    </div>
    <div class="form-group">
        <label for="inputEmail3" class="sr-only">${message("email")}</label>
        <input type="text" name="email" id="email" class="form-control" placeholder="Email" ng-model="email"
               value="username" ng-required="true" ng-minlength="5" ng-maxlength="255">

    </div>
    <div class="form-group">
        <label for="inputPassword3" class="sr-only">${message("password")}</label>
        <input type="password" name="password" id="password" class="form-control" ng-model="password"
               placeholder="Password" value="password" ng-required="true" ng-minlength="5" ng-maxlength="255">
    </div>
    <div class="form-group">
        <label for="inputPassword3" class="sr-only">${message("rememberMe")}</label>
        <input type="checkbox" name="rememberMe" class="form-control" checked>
    </div>
    <div class="form-group" style="padding-top: 10px">

        <button type="submit" class="btn btn-primary" ng-click="submitLogin(loginForm, $event)">${message("signIn")}</button>
        <button type="submit" class="btn btn-primary" ng-click="submitRegister(loginForm, $event)">${message("register")}</button>

    </div>
</form>

