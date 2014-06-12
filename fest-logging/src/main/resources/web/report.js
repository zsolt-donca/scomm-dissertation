var nodeIdGenerator = 0;
function buildExecutions(parentNodeId, execution, pushNode) {
    var data = {
        method: execution.children("method").first().text(),
        args: execution.children("args").first().text(),
        result: function () {
            var result = execution.children("result").first();
            return {
                returnResult: result.children("return-result").first().text(),
                exceptionResult: result.children("exception-result").first().text(),
                emptyResult: result.children("empty-result").length > 0
            }
        }(),
        startTime: new Date(execution.children("start-time").first().text().trim()),
        endTime: new Date(execution.children("end-time").first().text().trim()),
        screenshot: parentNodeId < 0 ? execution.find("screenshot").last().text() : execution.children("screenshot").last().text()
    };

    var nodeId = nodeIdGenerator++;
    pushNode(parentNodeId, nodeId, data);
    var invocations = execution.children("invocations").first();
    invocations.children().each(function (index, invocation) {
        buildExecutions(nodeId, $(invocation), pushNode);
    });
}

function addReport(xml) {

    var rows = [];

    function pushNode(parentNodeId, nodeId, data) {

        var row = document.createElement('tr');
        row.setAttribute("data-tt-id", nodeId);
        if (parentNodeId >= 0) {
            row.setAttribute("data-tt-parent-id", parentNodeId);
        }

        function pushCell(text) {
            var cell = document.createElement('td');
            row.appendChild(cell);
            cell.innerHTML = text;
            return cell;
        }

        pushCell(data.method);
        pushCell(data.args);

        if (data.result.returnResult) {
            pushCell(data.result.returnResult);
        } else if (data.result.exceptionResult) {
            var cell = pushCell(data.result.exceptionResult);
            cell.setAttribute('class', 'exception-result');
        } else if (data.result.emptyResult) {
            pushCell("");
        }

        pushCell(moment(data.startTime).format("HH:mm:ss.SSS"));
        pushCell(moment(data.endTime).format("HH:mm:ss.SSS"));
        var elapsed = moment.duration(moment(data.endTime).diff(data.startTime)).as('seconds');
        pushCell(elapsed);

        function screenshot(link) {
            return link ? '<a href="' + link + '">shot</a>' : '';
        }

        pushCell(screenshot(data.screenshot));

        rows.push(row);
    }

    var firstId = nodeIdGenerator;
    buildExecutions(-1, $(xml).children('execution').first(), pushNode);

    var table = $("#example-basic-expandable");
    table.treetable('loadBranch', null, rows);
    table.treetable('expandNode', firstId);
    table.treetable('collapseNode', firstId);
}
