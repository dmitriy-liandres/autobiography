<h1>
    Welcome to Autobiography site
</h1>
<#if loginError??>
<div class="form-group">
    <article>
        <div class="alert alert-danger login-error">
            Wrong username or password!
        </div>
    </article>
</div>
</#if>
<p>Please enter your username and password.</p>
<form class="form-horizontal login-form" role="form" name="loginForm" method="post" novalidate
      action="{{submitAction}}">
    <div class="form-group">
        <label ng-if="error">Incorrect email or password</label>
    </div>
    <div class="form-group">
        <label for="inputEmail3" class="sr-only">Email</label>
        <input type="text" name="email" id="email" class="form-control" placeholder="Email" ng-model="email"
               value="username" ng-required="true" ng-minlength="5" ng-maxlength="255">

    </div>
    <div class="form-group">
        <label for="inputPassword3" class="sr-only">Password</label>
        <input type="password" name="password" id="password" class="form-control" ng-model="password"
               placeholder="Password" value="password" ng-required="true" ng-minlength="5" ng-maxlength="255">
    </div>
    <div class="form-group">
        <label for="inputPassword3" class="sr-only">Remember me</label>
        <input type="checkbox" name="rememberMe" class="form-control" checked>
    </div>
    <div class="form-group" style="padding-top: 10px">

        <button type="submit" class="btn btn-primary" ng-click="submitLogin(loginForm, $event)">Sign in</button>
        <button type="submit" class="btn btn-primary" ng-click="submitRegister(loginForm, $event)">Register</button>

    </div>
</form>

