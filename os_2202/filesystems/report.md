## OBL4-OS
# Filesystems

## 1 File Systems
1. **Name two factors that are important in the design of a file system.**    
A file system should not store permanent data in RAM as this would disappear when the computer is shut off, so a reliable storage solution that has data safely stored in an event of power loss or crash is important.
A way to find files like named data is also necessary.


2. **Name some examples of file metadata.**    
Size, Size on disk, created, modified, accessed, attributes, location, file type, owner, and name are some examples of metadata.


## 2 Files and directories
### Consider a Fast File System (FFS) like Linux’s ext4.  

a. **Explain the difference between a hard link and a soft link in this file system. What is the length of the content of a soft link file?**  

Hard links contain a mirror copy of the data while soft links just contain the path (a link) to the original file.

The length of a soft link will naturally then be the size of the path link and any other metadata the system stores about the link.

To note is that a hard link doesn't link to the original in any way other than trough the inode (index node) that stores the metadata 


b. **What is the minimum number of references (hard links) for any given folder?**  

2 as these are the `.` and `..`  
Used often when wanting to cd upwards `cd ..` and run a program in local directory `./program.sh` 

c. **Consider a folder /tmp/myfolder containing 5 subfolders. How many references (hard links) does it have? Try it yourself on a Linux system and include the output. Use ls -ld /tmp/myfolder to view the reference count (hint, it’s the second column in the output).**  

This is the output i got:

```terminal
/tmp/myfolder $ ls -la                                     
total 0
drwxr-xr-x 1 jesper jesper 4096 Nov 20 15:59 ./
drwxrwxrwt 1 root   root   4096 Nov 20 15:59 ../
drwxr-xr-x 1 jesper jesper 4096 Nov 20 15:59 subfolder1/
drwxr-xr-x 1 jesper jesper 4096 Nov 20 15:59 subfolder2/
drwxr-xr-x 1 jesper jesper 4096 Nov 20 15:59 subfolder3/
drwxr-xr-x 1 jesper jesper 4096 Nov 20 15:59 subfolder4/
drwxr-xr-x 1 jesper jesper 4096 Nov 20 15:59 subfolder5/
```

As we can see there are 5 subfolders and 7 hard links. the 2 from before and one for each subfolder.

d. **Explain how spatial locality is acheived in a FFS.**  

If you need a file, you likely will need to access the next one nearby.  
FFS uses locality heuristics to find likely next candidates that on average speeds up the file system.

### NTFS - Flexible tree with extents  

a. **Explain the differences and use of resident versus non-resident attributes in NTFS.**  

*resident:*
stores contents in MFT record

*non-resident:* stores extent pointers in MFT record (content then stored in extents)

b. **Discuss the benefits of NTFS-style extents in relation to blocks used by FAT or FFS.**

For the user they will appreciate having directories and file names being able to be 255 characters long and hard disks over 2TB

From an architectural point of view there is better security and encryption.


### Explain how copy-on-write (COW) helps guard against data corruption.  

Simply never overwriting data when updating.




## 3 Security

###  Authentication

a. **Why is it important to hash passwords with a unique salt, even if the salt can be publicly known?**  
Prevents pre-computated hash attacks (rainbow table)

b. **Explain how a user can use a program to update the password database, while at the same time does not have read or write permissions to the password database file itself. What are the caveats of this?**  

We do this everyday when changing passwords to our accounts trough websites. The websites trust that we are the authorized user when we login and therefore give us access to change our password. We don't have access to the database as everything runs trough the user interface.

The caveats are there is no guaranteed way to ensure the person on the other side are who they are. One can use IP checks, 2 factor authentication, complicated passwords and email verification, still there is no way to ensure with 100% probability they are the authorized user.

###  Software vulnerabilities

a. **Describe the problem with the well-known gets() library call. Name another library call that is safe to use that accomplishes the same thing.**

`gets()` doesn't check the array bound so will try to fit 20 characters into a 14 character array which will cause problems. `fgets()` checks array bounds and is better to use. 

b. **Explain why a microkernel is statistically more secure than a monolithic kernel.**  
As the name implies it is a smaller kernel and will have less that can go wrong because it has less code running.
Referred to having a smaller attack surface.  