
var nodeIdGenerator = 0;
function buildExecutions(parentNodeId, execution, pushNode) {
    var call = execution.children[0].innerHTML;
    var args = execution.children[1].innerHTML;
    var result = execution.children[2].innerHTML;

    var nodeId = nodeIdGenerator++;
    pushNode(parentNodeId, nodeId, call, args, result);
    var invocations = execution.children[3];
    if (invocations != null) {
        for (var i = 0; i < invocations.children.length; i++) {
            var invocation = invocations.children[i];
            buildExecutions(nodeId, invocation, pushNode);
        }
    }
}

function addReport(xml) {

    var rows = [];
    function pushNode(parentNodeId, nodeId, call, args, result) {

        var row = document.createElement('tr');
        row.setAttribute("data-tt-id", nodeId);
        if (parentNodeId >= 0) {
            row.setAttribute("data-tt-parent-id", parentNodeId);
        }

        var callCell = document.createElement('td');
        row.appendChild(callCell);
        callCell.innerHTML = call;

        var argCell = document.createElement('td');
        row.appendChild(argCell);
        argCell.innerHTML = args;

        var resultCell = document.createElement('td');
        row.appendChild(resultCell);
        resultCell.innerHTML = result;

        rows.push(row);
    }

    var firstId = nodeIdGenerator;
    buildExecutions(-1, xml.documentElement, pushNode);

    var table = $("#example-basic-expandable");
    table.treetable('loadBranch', null, rows);
    table.treetable('expandNode', firstId);
    table.treetable('collapseNode', firstId);
}

$("#example-basic-expandable").treetable({ expandable: true });
