$(function(){ // on dom ready

var cy = cytoscape({
  container: document.getElementById('cy'),
  
  style: [
    {
      selector: 'node',
      css: {
        'content': 'data(id)',
        'text-valign': 'center',
        'text-halign': 'center'
      }
    },
    {
      selector: 'edge',
      css: {
        'target-arrow-shape': 'triangle'
      }
    },
    {
      selector: ':selected',
      css: {
        'background-color': 'red',
        'line-color': 'red',
        'target-arrow-color': 'red',
        'source-arrow-color': 'red'
      }
    }
  ],
  
  elements: {
    nodes: [
      { data: { id: 'thread_Root' } },
      { data: { id: 'task.line_12.id_90', parent: 'thread_Root' } },
      { data: { id: 'task.line_13.id_91', parent: 'thread_Root' } },
      { data: { id: 'task.line_14.id_92', parent: 'thread_Root' } },
    ],
    edges: [
      { data: { id: 'None-thread_Root', source: 'None', target: 'thread_Root' } },
      { data: { id: 'task.line_13.id_91-task.line_12.id_90', source: 'task.line_13.id_91', target: 'task.line_12.id_90' } },
      { data: { id: 'task.line_13.id_91-task.line_12.id_90', source: 'task.line_13.id_91', target: 'task.line_12.id_90' } },
      { data: { id: 'task.line_14.id_92-task.line_13.id_91', source: 'task.line_14.id_92', target: 'task.line_13.id_91' } },
      { data: { id: 'task.line_14.id_92-task.line_13.id_91', source: 'task.line_14.id_92', target: 'task.line_13.id_91' } },
      { data: { id: 'id-id', source: 'id', target: 'id' } },
    ]
  },
  
  layout: {
    name: 'breadthfirst',
  }
});

}); // on dom ready
