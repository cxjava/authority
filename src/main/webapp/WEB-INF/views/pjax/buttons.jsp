 <title>d</title>
<script type="text/javascript">
		$(document).ready(function() {
			buttons =function(){

			console.log($(".row-fluid").html());
			}

		});
		window.dd="ccc";
		</script>
 <section id="buttons">
  <div class="page-header">
    <h1>Buttons</h1>
  </div>

  <h2>Default buttons</h2>
  <p>
    Button styles can be applied to anything with the
    <code>.btn</code>
    class applied. However, typically you'll want to apply these to only
    <code>&lt;a&gt;</code>
    and
    <code>&lt;button&gt;</code>
    elements for the best rendering.
  </p>
  <table class="table table-bordered table-striped">
    <thead>
      <tr>
        <th>Button</th>
        <th>class=""</th>
        <th>Description</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>
          <button type="button" class="btn">Default</button>
        </td>
        <td>
          <code>btn</code>
        </td>
        <td>Standard gray button with gradient</td>
      </tr>
      <tr>
        <td>
          <button type="button" class="btn btn-primary">Primary</button>
        </td>
        <td>
          <code>btn btn-primary</code>
        </td>
        <td>
          Provides extra visual weight and identifies the primary action in a set of buttons
        </td>
      </tr>
      <tr>
        <td>
          <button type="button" class="btn btn-info">Info</button>
        </td>
        <td>
          <code>btn btn-info</code>
        </td>
        <td>Used as an alternative to the default styles</td>
      </tr>
      <tr>
        <td>
          <button type="button" class="btn btn-success">Success</button>
        </td>
        <td>
          <code>btn btn-success</code>
        </td>
        <td>Indicates a successful or positive action</td>
      </tr>
      <tr>
        <td>
          <button type="button" class="btn btn-warning">Warning</button>
        </td>
        <td>
          <code>btn btn-warning</code>
        </td>
        <td>Indicates caution should be taken with this action</td>
      </tr>
      <tr>
        <td>
          <button type="button" class="btn btn-danger">Danger</button>
        </td>
        <td>
          <code>btn btn-danger</code>
        </td>
        <td>Indicates a dangerous or potentially negative action</td>
      </tr>
      <tr>
        <td>
          <button type="button" class="btn btn-inverse">Inverse</button>
        </td>
        <td>
          <code>btn btn-inverse</code>
        </td>
        <td>
          Alternate dark gray button, not tied to a semantic action or use
        </td>
      </tr>
      <tr>
        <td>
          <button type="button" class="btn btn-link">Link</button>
        </td>
        <td>
          <code>btn btn-link</code>
        </td>
        <td>
          Deemphasize a button by making it look like a link while maintaining button behavior
        </td>
      </tr>
    </tbody>
  </table>

  <h4>Cross browser compatibility</h4>
  <p>
    IE9 doesn't crop background gradients on rounded corners, so we remove it. Related, IE9 jankifies disabled
    <code>button</code>
    elements, rendering text gray with a nasty text-shadow that we cannot fix.
  </p>

  <h2>Button sizes</h2>
  <p>
    Fancy larger or smaller buttons? Add
    <code>.btn-large</code>
    ,
    <code>.btn-small</code>
    , or
    <code>.btn-mini</code>
    for additional sizes.
  </p>
  <div class="bs-docs-example">
    <p>
      <button type="button" class="btn btn-large btn-primary">Large button</button>
      <button type="button" class="btn btn-large">Large button</button>
    </p>
    <p>
      <button type="button" class="btn btn-primary">Default button</button>
      <button type="button" class="btn">Default button</button>
    </p>
    <p>
      <button type="button" class="btn btn-small btn-primary">Small button</button>
      <button type="button" class="btn btn-small">Small button</button>
    </p>
    <p>
      <button type="button" class="btn btn-mini btn-primary">Mini button</button>
      <button type="button" class="btn btn-mini">Mini button</button>
    </p>
  </div>
  <pre class="prettyprint linenums"><ol class="linenums"><li class="L0"><span class="tag">&lt;p&gt;</span></li><li class="L1"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large btn-primary"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Large button</span><span class="tag">&lt;/button&gt;</span></li><li class="L2"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Large button</span><span class="tag">&lt;/button&gt;</span></li><li class="L3"><span class="tag">&lt;/p&gt;</span></li><li class="L4"><span class="tag">&lt;p&gt;</span></li><li class="L5"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-primary"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Default button</span><span class="tag">&lt;/button&gt;</span></li><li class="L6"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Default button</span><span class="tag">&lt;/button&gt;</span></li><li class="L7"><span class="tag">&lt;/p&gt;</span></li><li class="L8"><span class="tag">&lt;p&gt;</span></li><li class="L9"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-small btn-primary"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Small button</span><span class="tag">&lt;/button&gt;</span></li><li class="L0"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-small"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Small button</span><span class="tag">&lt;/button&gt;</span></li><li class="L1"><span class="tag">&lt;/p&gt;</span></li><li class="L2"><span class="tag">&lt;p&gt;</span></li><li class="L3"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-mini btn-primary"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Mini button</span><span class="tag">&lt;/button&gt;</span></li><li class="L4"><span class="pln"></span><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-mini"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Mini button</span><span class="tag">&lt;/button&gt;</span></li><li class="L5"><span class="tag">&lt;/p&gt;</span></li></ol></pre>
  <p>
    Create block level buttons—those that span the full width of a parent— by adding
    <code>.btn-block</code>
    .
  </p>
  <div class="bs-docs-example">
    <div class="well" style="max-width: 400px; margin: 0 auto 10px;">
      <button type="button" class="btn btn-large btn-block btn-primary">Block level button</button>
      <button type="button" class="btn btn-large btn-block">Block level button</button>
    </div>
  </div>
  <pre class="prettyprint linenums"><ol class="linenums"><li class="L0"><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large btn-block btn-primary"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Block level button</span><span class="tag">&lt;/button&gt;</span></li><li class="L1"><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large btn-block"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="tag">&gt;</span><span class="pln">Block level button</span><span class="tag">&lt;/button&gt;</span></li></ol></pre>

  <h2>Disabled state</h2>
  <p>Make buttons look unclickable by fading them back 50%.</p>

  <h3>Anchor element</h3>
  <p>
    Add the
    <code>.disabled</code>
    class to
    <code>&lt;a&gt;</code>
    buttons.
  </p>
  <p class="bs-docs-example">
    <a href="#" class="btn btn-large btn-primary disabled">Primary link</a>
    <a href="#" class="btn btn-large disabled">Link</a>
  </p>
  <pre class="prettyprint linenums"><ol class="linenums"><li class="L0"><span class="tag">&lt;a</span><span class="pln"></span><span class="atn">href</span><span class="pun">=</span><span class="atv">"#"</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large btn-primary disabled"</span><span class="tag">&gt;</span><span class="pln">Primary link</span><span class="tag">&lt;/a&gt;</span></li><li class="L1"><span class="tag">&lt;a</span><span class="pln"></span><span class="atn">href</span><span class="pun">=</span><span class="atv">"#"</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large disabled"</span><span class="tag">&gt;</span><span class="pln">Link</span><span class="tag">&lt;/a&gt;</span></li></ol></pre>
  <p>
    <span class="label label-info">Heads up!</span>
    We use
    <code>.disabled</code>
    as a utility class here, similar to the common
    <code>.active</code>
    class, so no prefix is required. Also, this class is only for aesthetic; you must use custom JavaScript to disable links here.
  </p>

  <h3>Button element</h3>
  <p>
    Add the
    <code>disabled</code>
    attribute to
    <code>&lt;button&gt;</code>
    buttons.
  </p>
  <p class="bs-docs-example">
    <button type="button" class="btn btn-large btn-primary disabled" disabled="disabled">Primary button</button>
    <button type="button" class="btn btn-large" disabled="">Button</button>
  </p>
  <pre class="prettyprint linenums"><ol class="linenums"><li class="L0"><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large btn-primary disabled"</span><span class="pln"></span><span class="atn">disabled</span><span class="pun">=</span><span class="atv">"disabled"</span><span class="tag">&gt;</span><span class="pln">Primary button</span><span class="tag">&lt;/button&gt;</span></li><li class="L1"><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn btn-large"</span><span class="pln"></span><span class="atn">disabled</span><span class="tag">&gt;</span><span class="pln">Button</span><span class="tag">&lt;/button&gt;</span></li></ol></pre>

  <h2>One class, multiple tags</h2>
  <p>
    Use the
    <code>.btn</code>
    class on an
    <code>&lt;a&gt;</code>
    ,
    <code>&lt;button&gt;</code>
    , or
    <code>&lt;input&gt;</code>
    element.
  </p>
  <form class="bs-docs-example">
    <a class="btn" href="">Link</a>
    <button class="btn" type="submit">Button</button>
    <input class="btn" type="button" value="Input">
    <input class="btn" type="submit" value="Submit"></form>
  <pre class="prettyprint linenums"><ol class="linenums"><li class="L0"><span class="tag">&lt;a</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn"</span><span class="pln"></span><span class="atn">href</span><span class="pun">=</span><span class="atv">""</span><span class="tag">&gt;</span><span class="pln">Link</span><span class="tag">&lt;/a&gt;</span></li><li class="L1"><span class="tag">&lt;button</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"submit"</span><span class="tag">&gt;</span><span class="pln">Button</span><span class="tag">&lt;/button&gt;</span></li><li class="L2"><span class="tag">&lt;input</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"button"</span><span class="pln"></span><span class="atn">value</span><span class="pun">=</span><span class="atv">"Input"</span><span class="tag">&gt;</span></li><li class="L3"><span class="tag">&lt;input</span><span class="pln"></span><span class="atn">class</span><span class="pun">=</span><span class="atv">"btn"</span><span class="pln"></span><span class="atn">type</span><span class="pun">=</span><span class="atv">"submit"</span><span class="pln"></span><span class="atn">value</span><span class="pun">=</span><span class="atv">"Submit"</span><span class="tag">&gt;</span></li></ol></pre>
  <p>
    As a best practice, try to match the element for your context to ensure matching cross-browser rendering. If you have an
    <code>input</code>
    , use an
    <code>&lt;input type="submit"&gt;</code>
    for your button.
  </p>

</section>