<div ng-controller="UserController">
    <input type="hidden" id="lang-input-id" value="${getServerLang()}">
    <table>
        <tr>
            <td valign="top">
            <#include "widgets/left-panel.ftl">
            </td>
            <td valign="top">
                <div ng-repeat="chapter in chapters">
                    <a href=""  ng-click="chapter[$index].visible = !chapter[$index].visible">{{$index + 1}}. {{chapter.name}}</a>

                    <div ng-repeat="subChapter in chapter.subChapters" ng-show="chapter[$parent.$index].visible">
                        <a style="margin-left:5px" href="" id="subChapter-{{chapter.id}}-{{subChapter.id}}-id" ng-click="loadSubChapterAnswer(chapter.id, subChapter.id)">{{$index + 1}}. {{subChapter.name}}</a>

                    </div>
                </div>
                <div id="autobioInterestingTextBlockId" ng-show="anySubChapterClicked">
                    <textarea name="autobioText" id="autobioText"></textarea>
                    <button type="button" class="btn btn-primary"
                            ng-click="saveAutobioInterestingText()">${message("save")}</button>
                </div>

            </td>
        </tr>

    </table>

    <a href="/autobiography-interesting/${loggedInPersonId}">${message("autobio-interesting.view")}</a>
</div>