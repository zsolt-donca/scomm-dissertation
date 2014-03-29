
var nodeIdGenerator = 0;
function buildExecutions(parentNodeId, execution, pushNode) {
    var data = {
        call: execution.children[0].innerHTML,
        args: execution.children[1].innerHTML,
        result: execution.children[2].innerHTML
    };

    var nodeId = nodeIdGenerator++;
    pushNode(parentNodeId, nodeId, data);
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
    function pushNode(parentNodeId, nodeId, data) {

        var row = document.createElement('tr');
        row.setAttribute("data-tt-id", nodeId);
        if (parentNodeId >= 0) {
            row.setAttribute("data-tt-parent-id", parentNodeId);
        }

        var callCell = document.createElement('td');
        row.appendChild(callCell);
        callCell.innerHTML = data.call;

        var argCell = document.createElement('td');
        row.appendChild(argCell);
        argCell.innerHTML = data.args;

        var resultCell = document.createElement('td');
        row.appendChild(resultCell);
        resultCell.innerHTML = data.result;

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
