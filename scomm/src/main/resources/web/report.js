
var nodeIdGenerator = 0;
function buildExecutions(parentNodeId, execution, pushNode) {
    var data = {
        method: execution.getElementsByTagName("method")[0].innerHTML,
        args: execution.getElementsByTagName("args")[0].innerHTML,
        result: execution.getElementsByTagName("result")[0].children[0].innerHTML,
        startTime : new Date(execution.getElementsByTagName("start-time")[0].innerHTML),
        endTime: new Date(execution.getElementsByTagName("end-time")[0].innerHTML)
    };

    var nodeId = nodeIdGenerator++;
    pushNode(parentNodeId, nodeId, data);
    var invocations = execution.getElementsByTagName("invocations")[0];
    if (invocations != null) {
        for (var i = 0; i < invocations.children.length; i++) {
            var invocation = invocations.children[i];
            buildExecutions(nodeId, invocation, pushNode);
        }
    }
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

        rows.push(row);
    }

    var firstId = nodeIdGenerator;
    buildExecutions(-1, xml.documentElement, pushNode);

    var table = $("#example-basic-expandable");
    table.treetable('loadBranch', null, rows);
    table.treetable('expandNode', firstId);
    table.treetable('collapseNode', firstId);
}


