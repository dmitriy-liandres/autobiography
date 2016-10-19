<div ng-if="messageSent">${message("contact.sent")}</div>
<div  ng-if="!messageSent">
    <p ng-if="messageSent">${message("contact.description")}</p>
    <form class="form-horizontal login-form" role="form" name="contactForm" method="post" novalidate
          action="#">
        <div class="form-group">
            <label for="inputEmail3" class="sr-only">${message("contact.user.email")}</label>
            <input type="email" name="email" id="email" class="form-control"
                   placeholder="${message("contact.user.email")}"
                   ng-model="contactView.email"
                   ng-required="true" ng-minlength="5" ng-maxlength="255">

        </div>
        <div class="form-group">
            <label for="inputEmail3" class="sr-only">${message("contact.user.name")}</label>
            <input type="text" name="name" id="name" class="form-control" placeholder="${message("contact.user.name")}"
                   ng-model="contactView.name"
                   ng-required="true" ng-minlength="2" ng-maxlength="255">

        </div>
        <div class="form-group">
            <label for="inputEmail3" class="sr-only">${message("contact.user.message")}</label>
        </div>
        <textarea style="width: 252px; height: 58px;" name="message" id="message" class="form-control" placeholder="${message("contact.user.message")}"
                  ng-model="contactView.message"
                  ng-required="true" ng-minlength="5" ng-maxlength="1000">  </textarea>

        <div class="form-group">
            <button type="button" class="btn btn-primary"
                    ng-click="contact(contactForm)">${message("contact.send")}</button>
        </div>
    </form>
</div>