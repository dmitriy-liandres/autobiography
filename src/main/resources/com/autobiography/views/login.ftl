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
<form class="form-horizontal login-form" role="form" method="post" action="/login" ng-submit="submit()" >
    <div class="row">
        <div class="col-md-6">
            <div class="form-group">
                <label for="inputEmail3" class="sr-only">Email</label>

                <div class="col-sm-12">
                    <input type="text" name="email" class="form-control"  placeholder="Email" value="username">
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="form-group">
                <label for="inputPassword3" class="sr-only">Password</label>

                <div class="col-sm-12">
                    <input type="password" name="password" class="form-control"
                           placeholder="Password" value="password">
                </div>

            </div>
        </div>

    </div>
    <div class="form-group" style="padding-top: 10px">
        <div class="col-sm-12">
            <button type="submit" class="btn btn-primary">Sign in</button>
        </div>
    </div>
</form>

