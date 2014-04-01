
var nodeIdGenerator = 0;
function buildExecutions(parentNodeId, execution, pushNode) {
    var data = {
        method: execution.children("method").first().text(),
        args: execution.children("args").first().text(),
        result: execution.children("result").find().first().text(),
        startTime : new Date(execution.children("start-time").first().text()),
        endTime: new Date(execution.children("end-time").first().text()),
        beforeScreenshot: parentNodeId < 0 ? execution.find("before-screenshot").first().text() : execution.children("before-screenshot").first().text(),
        afterScreenshot: parentNodeId < 0 ? execution.find("after-screenshot").last().text() : execution.children("after-screenshot").last().text()
    };

    var nodeId = nodeIdGenerator++;
    pushNode(parentNodeId, nodeId, data);
    var invocations = execution.children("invocations").first();
    invocations.children().each(function(index, invocation) {
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
        }

        pushCell(data.method);
        pushCell(data.args);
        pushCell(data.result);
        pushCell(moment(data.startTime).format("HH:mm:ss.SSS"));
        pushCell(moment(data.endTime).format("HH:mm:ss.SSS"));
        var elapsed = moment.duration(moment(data.endTime).diff(data.startTime)).as('seconds');
        pushCell(elapsed);

        function screenshot(link) {
            return link ? '<a href="' + link + '">shot</a>' : '';
        }

        pushCell(screenshot(data.beforeScreenshot));
        pushCell(screenshot(data.afterScreenshot));

        rows.push(row);
    }

    var firstId = nodeIdGenerator;
    buildExecutions(-1, $(xml).children('execution').first(), pushNode);

    var table = $("#example-basic-expandable");
    table.treetable('loadBranch', null, rows);
    table.treetable('expandNode', firstId);
    table.treetable('collapseNode', firstId);
}
