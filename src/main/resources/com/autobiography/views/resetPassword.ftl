${message("resetPassword.message")}
<form class="form-horizontal login-form" role="form" name="loginForm" method="post" novalidate
      action="/reset-password">

    <p ng-if="error!=null">${message("resetPassword.incorrectEmail")}</p>


    <div class="form-group">
        <label for="inputEmail3" class="sr-only">${message("email")}</label>
        <input type="text" name="email" id="email" class="form-control" placeholder="Email" ng-model="email"
               value="username" ng-required="true" ng-minlength="5" ng-maxlength="255">

    </div>

    <div class="form-group" style="padding-top: 10px">

        <button type="submit" class="btn btn-primary">${message("resetPassword.sendPassword")}</button>

    </div>
</form>

