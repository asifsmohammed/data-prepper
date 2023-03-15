
* __Use Netty version supplied by dependencies (#2031) (#2373)__

    [opensearch-trigger-bot[bot]](mailto:98922864+opensearch-trigger-bot[bot]@users.noreply.github.com) - Mon, 13 Mar 2023 17:43:08 -0500
    
    EAD -&gt; refs/heads/2.1.1-change-log, refs/remotes/upstream/2.1
    * Removed old constraints on Netty which were resulting in pulling in older
    version of Netty. Our dependencies (Armeria and AWS SDK Java client) are
    pulling in newer versions so these old configurations are not necessary
    anymore.
     Signed-off-by: David Venable &lt;dlv@amazon.com&gt;
    (cherry picked from commit eacbe0047e43df683b0853d3cecc849c77a9d69c)
     Co-authored-by: David Venable &lt;dlv@amazon.com&gt;

* __Fix: Fixed IllegalArgumentException in PluginMetrics  (#2369) (#2370)__

    [opensearch-trigger-bot[bot]](mailto:98922864+opensearch-trigger-bot[bot]@users.noreply.github.com) - Thu, 9 Mar 2023 14:25:01 -0600
    
    
    * Fix: Fixed IllegalArgumentException in PluginMetrics caused by pipeline name
     Signed-off-by: Asif Sohail Mohammed &lt;nsifmoh@amazon.com&gt;
    (cherry picked from commit 4a9a299ae6c5aeba9cf1b1d7f1ee2aa4c963f22f)
     Co-authored-by: Asif Sohail Mohammed &lt;nsifmoh@amazon.com&gt;

* __Updated version to 2.1.1 (#2365)__

    [Asif Sohail Mohammed](mailto:nsifmoh@amazon.com) - Mon, 6 Mar 2023 12:47:29 -0600
    
    
    Signed-off-by: Asif Sohail Mohammed &lt;nsifmoh@amazon.com&gt;

* __FIX: traceState not required in Link (#2363) (#2364)__

    [opensearch-trigger-bot[bot]](mailto:98922864+opensearch-trigger-bot[bot]@users.noreply.github.com) - Fri, 3 Mar 2023 17:24:46 -0600
    
    
    Signed-off-by: George Chen &lt;qchea@amazon.com&gt;
    (cherry picked from commit 6d4eb427e921eaa08ca2de54e02dc3df92a4e096)

* __Added 2.1 change log (#2360) (#2361)__

    [opensearch-trigger-bot[bot]](mailto:98922864+opensearch-trigger-bot[bot]@users.noreply.github.com) - Thu, 2 Mar 2023 15:22:21 -0600
    
    
    Signed-off-by: Asif Sohail Mohammed &lt;nsifmoh@amazon.com&gt;
    (cherry picked from commit 567e3bf5efbe89731a79c5e55f08f44270d02bd6)
     Co-authored-by: Asif Sohail Mohammed &lt;nsifmoh@amazon.com&gt;


