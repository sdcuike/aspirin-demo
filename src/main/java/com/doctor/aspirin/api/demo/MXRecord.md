MX record

https://en.wikipedia.org/wiki/MX_record



A mail exchanger record (MX record) is a type of resource record in the Domain Name System that specifies a mail server responsible for accepting email messages on behalf of a recipient's domain, and a preference value used to prioritize mail delivery if multiple mail servers are available. The set of MX records of a domain name specifies how email should be routed with the Simple Mail Transfer Protocol (SMTP).

Contents  [hide] 
1   Overview
2   MX preference, distance, and priority
2.1 The basics
2.2 Load distribution among an array of the mail servers
2.3 The backup MX
2.4 Priority
2.5 Spammers
3   History of fallback to address record
4   Standards documents
5   See also
6   References
Overview[edit]
Resource records are the basic information element of the Domain Name System (DNS). They are distinguished by a type identification (A, MX, NS, etc.) and a DNS class (Internet, CHAOS, etc.). The records have a validity period (time-to-live) assigned to them, indicating when the information they hold must be refreshed from an authoritative name server. Resource records are organized within the DNS based on their name field, which is a fully qualified domain name (FQDN) of a node in the DNS tree. In the case of an MX record, this specifies the domain name of a mail recipient's email address, i.e. the portion after the @ symbol that delimits the recipient's account name.

The characteristic payload information of an MX record is the fully qualified domain name of a mail host and a preference value. The host name must map directly to one or more address record (A, or AAAA) in the DNS, and must not point to any CNAME records.[1]

When an e-mail message is sent through the Internet, the sending mail transfer agent (MTA) queries the Domain Name System for MX records of each recipient's domain name. This query returns a list of host names of mail exchange servers accepting incoming mail for that domain and their preferences. The sending agent then attempts to establish an SMTP connection.

The MX mechanism provides the ability to run multiple mail servers for a single domain, and allows administrators to specify an order in which they should be tried. This ability to run multiple mail servers proves very valuable for high-availability clusters of inexpensive mail gateways, which can then process hundreds of messages per second in aggregate to quarantine or remove spam and/or viruses.

The MX mechanism does not grant the ability to provide mail service on alternative port numbers, nor does it provide the ability to distribute mail delivery across a set of unequal-priority mail servers by assigning a weighting value to each one. MX can be used to distribute delivery across equal-priority mail servers.[2]

MX preference, distance, and priority[edit]
According to RFC 5321, the lowest-numbered records are the most preferred.[3] This phrasing can be confusing, and so the preference number is sometimes referred to as the distance: smaller distances are more preferable. An older RFC, RFC 974, indicates that when the preference numbers are the same for two servers, they have the same priority, hence those two terms are used interchangeably.

The basics[edit]
In the simplest case, a domain may have just one mail server. For example, if an MTA looks up the MX records for example.com, and the DNS server replied with only mail.example.com with a preference number of 50, then the MTA will attempt delivery of the mail to the server listed. In this case, the number 50 could have been any integer permitted by the SMTP specification.

But when more than one server is returned for an MX query, the preference number for each record dictates the relative priority of the listed server. When a remote client (typically another mail server) does an MX lookup for the domain name, it gets a list of servers and their preference numbers. The smallest preference number has the highest priority and any server with the smallest preference number must be tried first. To provide reliable mail transmission, the SMTP client must be able to try (and retry) each of the relevant addresses in this list in order, until a delivery attempt succeeds.[3] If there is more than one MX record with the same preference number, all of those must be tried before moving on to lower-priority entries.

Load distribution among an array of the mail servers[edit]
A technique used to distribute the load of incoming mail over an array of servers is to return the same preference number for each server in the set. When determining which server of equal preference to send mail to, "the sender-SMTP MUST randomize them to spread the load across multiple mail exchangers for a specific organization", unless there is a clear reason to favor one.[3] Note that multihomed servers are treated differently, since in this case any randomization is assumed to have been applied already by the name server. This technique mainly addresses routing problems; other types of server load can be addressed by using an SMTP proxy.

The other alternative mentioned in the RFC is to use what appears to be a multihomed A record for a mail server. It may in fact be an array of mail servers that share the same host name. This method places the burden on the DNS system rather than the SMTP-sender to perform the load balancing, which in this case will present a list of IP addresses in a specific order to the clients querying the A record of the mail exchanger. Since the RFC requires that the SMTP-sender use the order given in the A record query, the DNS server is free to carefully manipulate its balancing based on any method, including round robin DNS, mail server load, or some undisclosed priority scheme.

The backup MX[edit]
A target server, i.e. one that knows how to deliver to the relevant user's e-mail mailbox is typically one which is the most preferred. Lower priority servers, a.k.a. backup MX or secondary MX, usually keep the messages in a queue waiting for the primary server to become available. If both servers are online or in some way connected to one another, the backup MX will typically queue a message briefly and immediately forward it to the primary MX. The backup MX acts as a store and forward mail server.

Priority[edit]
The MX priority determines the order in which the Servers are supposed to be contacted (if several servers with different priorities are given): The Server(s) with the highest priority (and the lowest preference number) will be tried first. In DNS records, typically, the preference number is set and shown - but often erroneously labeled "priority". A common misconception about the MX preference ordering is that it is intended to increase the likelihood that mail may be delivered; however, merely having multiple MX records with the same preference provides this benefit. Because the MX preference ordering specifies that some servers should be tried first, it is, if anything, a means of establishing load imbalance. Another common misinterpretation of MX preference ordering is that it is intended to provide a means of "failover" in the case of server overload. While it can be used that way, it is a poor resource management technique because it intentionally creates overload and does not fully utilize the available hardware. Assigning the same preference value to all of the available servers provides the same benefit and may even help avoid overload situations and thereby increase system throughput by decreasing latency.

The SMTP protocol establishes a store-and-forward network, and if a domain's mail servers are all offline, sending servers are required to queue messages destined for that domain to retry later. However, these sending servers have no way of being notified that a previously offline domain's servers are now available. The sending servers will only discover that the domain is available whenever delivery of the delayed messages is next attempted. The delay between when a domain's servers come online and when delayed messages are finally delivered can be anywhere from minutes to days, depending on the retry schedule of the sending servers. This is the problem that backup MX records are uniquely qualified to solve. The idea is that the servers listed as secondary MX servers have some out-of-band way of knowing when the primary servers are back online. Thus, they are a more useful place to queue messages when the primary servers are offline than the original sender's queue.

The question then becomes: if the secondary servers are still online, why not give them the same priority as the rest of the domain's MX records? Secondary servers are ones that, for whatever reason, cannot or should not be used normally, but that can be used if the primary servers are offline. Reasons for them to not be used normally can vary widely, but here are some examples:

the backup server is owned by a different company or organization
the backup server does not have direct access to the primary mail storage
the backup server cannot determine valid recipient addresses
the backup server's Internet bandwidth costs more
the backup server has significantly less Internet bandwidth
the backup server has a high-latency Internet connection
Spammers[edit]
Spammers frequently direct mail to one of the backup (high distance) MX servers of a domain first in order to avoid any anti-spam filters that may be running on the primary (lowest distance/highest preference) exchanger. Backup MX servers often have different anti-spam software, and using them can hide the spammer's IP address from the primary MX servers. This can be foiled by using bogus high-distance MX servers.

Alternatively, sometimes spammers only target the lowest-distance MX records for domains, and do not fall back to higher-distance MX records when the lowest-distance MX records are unreachable. A technique called nolisting will defeat this behavior.

The SMTP RFC[3] is ambiguous about exactly what kinds of delivery failure must result in re-attempting delivery via more distant MX records (those with higher preference values).

For example, sometimes servers indicate temporary failures, either by explicitly sending a 4xx error or by ending the connection unexpectedly (which must be treated as a 451 error, according to Section 3.8 of the RFC). If there is a temporary failure, should a more distant MX record be attempted, or should the server wait and retry later? According to Section 4.5.4.1: The sender MUST delay retrying a particular destination after one attempt has failed.

But when the sender retries later, the RFC is silent about whether the sender should retry the same server that gave the previous temporary error or a more distant MX record. It does say, in Section 5.1: When the lookup succeeds, the mapping can result in a list of alternative delivery addresses rather than a single address, because of multiple MX records, multihoming, or both. To provide reliable mail transmission, the SMTP client MUST be able to try (and retry) each of the relevant addresses in this list in order, until a delivery attempt succeeds.

It is not specific about what should cause the sender to use a higher-preference MX record, merely that the sender must be able to use higher-preference MX records. Some servers (such as Sendmail and Postfix 2.1 or later[4]) will attempt the next-furthest MX server after some types of temporary delivery failures, such as greeting failures.[5] Other servers (such as qmail and Postfix 2.0 or earlier) will only use more distant MX records if the servers specified in the shortest-distance MX records could not be contacted at all.

Both behaviors are valid, since the RFC is not specific. Re-attempting with more distant MX records makes a lot of sense if the primary MX's failure is considered non-authoritative; that is, if there is an expectation that the message may yet be delivered by the backup MX servers. This is often phrased as "if the alternative is giving up and not delivering the mail, why not try the higher-preference MXs?"[6] However, if the primary MX's failure is considered authoritative (i.e. it is the primary server for a non-arbitrary reason), attempting to deliver to secondary MX servers is not only a waste of time but potentially a waste of expensive resources, depending on the reason why the secondary servers have higher preference values.

The different MX-handling behaviors of email servers (MTAs) often comes up in discussions of nolisting and similar anti-spam strategies that rely on manipulating the MX order and exercising MTA failure handling mechanisms.

Regardless of how one interprets the RFCs there is an advantage to choosing to deliver to higher numbered MX records when receiving a 4xx error from a primary mail server. If the primary server is overloaded or experiencing some other temporary failure the backup server can accept the email and process it. This means the email is delivered faster than waiting to retry the primary server until it recovers. Some front end spam filtering services apply gray listing techniques on only the primary server to discourage spam bot email. Sending servers that retry the higher MX records will be able to deliver their outgoing mail immediately, while servers like qmail will be delayed typically for an hour till the primary server allows it. So servers that retry higher numbered MX records gain a performance advantage.

History of fallback to address record[edit]
RFC 821 was published in 1982. It makes only passing references to DNS, because at the time the transition from HOSTS.TXT to the DNS had not yet started. RFC 883, the first description of the DNS, was published over a year later in late 1983. It described the experimental and little used MD and MF records. According to RFC 897 and RFC 921, the transition to DNS started in 1983, but HOSTS.TXT was not scheduled to be phased out until the end of 1985 and was not totally phased out until the late 1990s.

In January 1986, RFC 973 and RFC 974 deprecated the MD and MF records, replaced them with MX, and defined the MX lookup with fallback to A. RFC 974 recommends that clients do a WKS lookup[7] on each MX host to see if it actually supports SMTP and discard the MX entry if not. However, RFC 1123 changed this to say that WKS should not be checked.

This means that SMTP had been in use for at least a year using HOSTS.TXT, and then another couple of years using A, MD, and MF, before MX came along. MD and MF were hard to use, so most people just used the A record. Under the circumstances, MX without fallback to A would not have worked because of the substantial installed base of mail servers using A records. The early use of MX was to identify gateways to other networks, but it did not come into wide use until the DNS was well established in the early 1990s.[8]

RFC 5321 sec. 5 states:

SMTP clients must look up an MX record;
if (and only if) no MX record for the domain is present, treat the domain as if it had an MX record with the given domain as the target hostname and a preference value of 0
perform A or AAAA lookups as required to determine the IP address of the target hostname
Standards documents[edit]
RFC 974 (1986), Mail Routing and the Domain System (obsoleted)
RFC 1035 (1987), Domain Names - Implementation and Specification
RFC 5321 (2001), Simple Mail Transfer Protocol
RFC 1912 (1996), Common DNS Operational and Configuration Errors
RFC 7505 (2015), A "Null MX" No Service Resource Record for Domains That Accept No Mail
See also[edit]
SRV record - a similar DNS record for advertising other network services
Sender Policy Framework - somewhat of a reverse MX record, saying where mail gets sent from, rather than sent to.
List of DNS record types
A record
Nolisting
References[edit]
Jump up ^ RFC 2181, Section 10.3, Clarifications to the DNS Specification, R. Elz, R. Bush (July 1997)
Jump up ^ HOWTO - Configure Round Robin and Load Balancing, Page modified: February 28 2014., zytrax.com
^ Jump up to: a b c d RFC 5321
Jump up ^ If the primary MX responds, but fails mid-transaction, Postfix 1.2 and 2.0 will not try a backup MX., Re: does not change to mx with lower priority, From: Victor Duchovni (Victor.DuchovniMorganStanley.com) Date: Fri Nov 11 2005
Jump up ^ A greeting failure is an error-code that is sent instead of or in response to the standard SMTP greeting handshake.
Jump up ^ This argument ignores the fact that in most cases (such as with Sendmail and Postfix) the more distant MXs may be used long before the server gets close to giving up.
Jump up ^ Craig Partridge (January 1986). MAIL ROUTING AND THE DOMAIN SYSTEM. IETF. RFC 974. Retrieved 18 November 2011. "For each MX, a WKS query should be issued to see if the domain name listed actually supports the mail service desired. MX RRs which list domain names which do not support the service should be discarded. This step is optional, but strongly encouraged."
Jump up ^ This section is adapted from John Levine ietf-smtp message
Categories: Domain name systemEmail