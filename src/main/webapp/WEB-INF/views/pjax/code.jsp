<!-- Code
        ================================================== -->
        <title>a</title>
        <script type="text/javascript">
		$(document).ready(function() {
			buttons =function(){

			console.log($(".row-fluid").html());
			}

		});
		window.dd="aaa";
		</script>
<section id="code">
  <div class="page-header">
    <h1>Code</h1>
  </div>

  <h2>Inline</h2>
  <p>
    Wrap inline snippets of code with
    <code>&lt;code&gt;</code>
    .
  </p>
  <div class="bs-docs-example">
    For example,
    <code>&lt;section&gt;</code>
    should be wrapped as inline.
  </div>
  <pre class="prettyprint linenums">
For example, &lt;code&gt;&amp;lt;section&amp;gt;&lt;/code&gt; should be wrapped as inline.
</pre>

  <h2>Basic block</h2>
  <p>
    Use
    <code>&lt;pre&gt;</code>
    for multiple lines of code. Be sure to escape any angle brackets in the code for proper rendering.
  </p>
  <div class="bs-docs-example">
    <pre>&lt;p&gt;Sample text here...&lt;/p&gt;</pre>
  </div>
  <pre class="prettyprint linenums" style="margin-bottom: 9px;">
&lt;pre&gt;
  &amp;lt;p&amp;gt;Sample text here...&amp;lt;/p&amp;gt;
&lt;/pre&gt;
</pre>
  <p>
    <span class="label label-info">Heads up!</span>
    Be sure to keep code within
    <code>&lt;pre&gt;</code>
    tags as close to the left as possible; it will render all tabs.
  </p>
  <p>
    You may optionally add the
    <code>.pre-scrollable</code>
    class which will set a max-height of 350px and provide a y-axis scrollbar.
  </p>
</section>