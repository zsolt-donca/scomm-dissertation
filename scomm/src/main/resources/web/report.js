
var executionNodes = [];

function pushNode(parentId, call, args, result) {
    var nodeId = executionNodes.length;
    var executionNode = {
        nodeId: nodeId,
        parentNodeId: parentId,
        call: call,
        args: args,
        result: result
    };
    executionNodes.push(executionNode);
    return executionNode;
}

function buildExecutions(parentId, execution) {
    var call = execution.children[0].innerHTML;
    var args = execution.children[1].innerHTML;
    var result = execution.children[2].innerHTML;
    var executionNode = pushNode(parentId, call, args, result);
    var invocations = execution.children[3];
    if (invocations != null) {
        for (var i = 0; i < invocations.children.length; i++) {
            var invocation = invocations.children[i];
            buildExecutions(executionNode.nodeId, invocation);
        }
    }
}

function addReport(xml) {

    var from = executionNodes.length;
    buildExecutions(-1, xml.documentElement);
    var to = executionNodes.length;
    var tableComponent = document.getElementById("example-basic-expandable");

    var rows = [];
    for (var i = from ; i < to; i++) {
        var executionNode = executionNodes[i];

        var row = document.createElement('tr');/* tableComponent.insertRow(-1);*/
        row.setAttribute("data-tt-id", executionNode.nodeId);
        if (executionNode.parentNodeId >= 0) {
            row.setAttribute("data-tt-parent-id", executionNode.parentNodeId);
        }

        var callCell = document.createElement('td');
        row.appendChild(callCell);
        callCell.innerHTML = executionNode.call;

        var argCell = document.createElement('td');
        row.appendChild(argCell);
        argCell.innerHTML = executionNode.args;

        var resultCell = document.createElement('td');
        row.appendChild(resultCell);
        resultCell.innerHTML = executionNode.result;

        rows.push(row);
    }
    var table = $("#example-basic-expandable");
    table.treetable('loadBranch', null, rows);
    var nodeId = executionNodes[from].nodeId;
    table.treetable('expandNode', nodeId);
    table.treetable('collapseNode', nodeId);
}

$("#example-basic-expandable").treetable({ expandable: true });
